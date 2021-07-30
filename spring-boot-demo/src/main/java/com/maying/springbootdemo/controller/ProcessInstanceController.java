package com.maying.springbootdemo.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * description
 */
@RestController
public class ProcessInstanceController {

    @GetMapping("/hello")
    public String hello(){
        return "hello world!";
    }
}
