package com.example.demo.controller;

import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/testSpringBoot")
public class HelloController {

    @Autowired
    UserService userService;

    @RequestMapping(value = "/hello")
    public String hello(){
        return "hello SpringBoot";
    }

    @GetMapping("getUser/{id}")
    public String getUser(@PathVariable int id){
        return userService.sel(id).toString();
    }
}
