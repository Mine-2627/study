package com.maying.designMode.single;

/**
 * 饿汉式单例
 */
public class Hungry {

    //加载时即创建，造成浪费空间
    private byte[] data1 = new byte[1024*1024];
    private byte[] data2 = new byte[1024*1024];
    private byte[] data3 = new byte[1024*1024];
    private byte[] data4 = new byte[1024*1024];
    private byte[] data5 = new byte[1024*1024];

    private Hungry(){

    }

    private final static Hungry HUNGRY = new Hungry();

    public static Hungry getInstance(){
        return HUNGRY;
    }
}
