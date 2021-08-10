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
    //    上述为双重锁懒汉模式
    //    如果编写的是多线程程序，则不要删除上例代码中的关键字 volatile 和 synchronized，否则将存在线程非安全的问题。
    //    如果不删除这两个关键字就能保证线程安全，但是每次访问时都要同步，会影响性能，且消耗更多的资源

    //单线程下单例是ok的
    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            new Thread(()->{
                LazyMan.getInstance();
            }).start();
        }
    }

}
