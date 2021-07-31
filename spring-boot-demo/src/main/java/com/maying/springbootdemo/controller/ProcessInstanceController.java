package com.maying.springbootdemo.controller;


import com.maying.springbootdemo.domain.entity.User;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * description
 */
@Api(tags = "流程实例")
@RestController
public class ProcessInstanceController {

    @ApiOperation("hello world 方法")
    @GetMapping("/hello")
    public String hello(){
        return "hello world!";
    }

    @ApiOperation("获取用户 方法")
    @GetMapping("/getUser")
    public User getUser(String userName){
        User user = new User();
        user.setUserName(userName);
        return user;
    }

    @ApiOperation("获取用户2方法")
    @PostMapping("/getUser2")
    public User getUser2(@RequestBody User user){
        return user;
    }

}
