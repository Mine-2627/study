package com.maying.designMode.single;

/**
 * 懒汉式单例
 */
public class LazyMan {

    private LazyMan(){
        System.out.println(Thread.currentThread().getName()+" OK");
    }

    private volatile static LazyMan lazyMan;


    //DCL模式的优点就是，只有在对象需要被使用时才创建，第一次判断 INSTANCE == null为了避免非必要加锁，
    // 当第一次加载时才对实例进行加锁再实例化。这样既可以节约内存空间，又可以保证线程安全。
    // 但是，由于jvm存在乱序执行功能，DCL也会出现线程不安全的情况
    public static LazyMan getInstance(){
        //双重检测锁模式
        if(lazyMan == null){
            synchronized (LazyMan.class){
                if(lazyMan == null){
                    //不是一个原子性操作
                    lazyMan = new LazyMan();
                    //1.分配内存空间
                    //2.执行构造方法，初始化对象
                    //3.把这个对象指向这个空间
                    //可能在内存分配的时候，新开了一个线程B，B指向一片虚无的空间，由于指令重排出现问题，所以需要对lazyMan进行volatile
                }
            }
        }
        return lazyMan;
    }
    //   这个步骤，其实在jvm里面的执行分为三步：
    //
    //  1.在堆内存开辟内存空间。
    //  2.在堆内存中实例化SingleTon里面的各个参数。
    //  3.把对象指向堆内存空间。
    //
    //由于jvm存在乱序执行功能，所以可能在2还没执行时就先执行了3，如果此时再被切换到线程B上，由于执行了3，INSTANCE 已经非空了，
    // 会被直接拿出来用，这样的话，就会出现异常。这个就是著名的DCL失效问题。
    //
    //不过在JDK1.5之后，官方也发现了这个问题，故而具体化了volatile，即在JDK1.6及以后，
    // 只要定义为private volatile static SingleTon  INSTANCE = null;就可解决DCL失效问题。
    // volatile确保INSTANCE每次均在主内存中读取，这样虽然会牺牲一点效率，但也无伤大雅


    //单线程下单例是ok的
    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            new Thread(()->{
                LazyMan.getInstance();
            }).start();
        }
    }

}
