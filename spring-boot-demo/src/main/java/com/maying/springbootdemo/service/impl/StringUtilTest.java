package com.maying.springbootdemo.service.impl;

import java.util.ArrayList;
import java.util.List;

/**
 * description
 */
public class StringUtilTest {


    public static void main(String[] args) {
        List<String> list = new ArrayList<>();
        list.add("111");
        list.add("222");
        list.add("333");
        String s = String.join("|", list);
        System.out.println(s);
    }
}
