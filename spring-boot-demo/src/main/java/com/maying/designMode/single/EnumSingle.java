package com.maying.designMode.single;

import sun.reflect.ConstructorAccessor;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * 枚举模式
 */
public enum  EnumSingle {

    INSTANCE;

    public EnumSingle getInstance(){
        return INSTANCE;
    }
}


class Test{

    public static void main(String[] args) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        EnumSingle enumSingle1 = EnumSingle.INSTANCE;
        Constructor<EnumSingle> declaredConstructor = EnumSingle.class.getDeclaredConstructor(String.class,int.class);
        declaredConstructor.setAccessible(true);

        //Cannot reflectively create enum objects 枚举类无法被反射破坏
        EnumSingle enumSingle2 = declaredConstructor.newInstance("111",2);
        System.out.println(enumSingle1);
        System.out.println(enumSingle2);
    }
}