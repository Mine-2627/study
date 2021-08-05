package com.maying.springbootdemo.controller;

import com.maying.springbootdemo.domain.entity.User;
import com.maying.springbootdemo.infra.mapper.UserMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * description
 */
@Api(tags = "用户test")
@RestController
public class UserController {

    @Autowired
    private UserMapper userMapper;

    @ApiOperation("查询用户列表")
    @GetMapping("/list")
    public ResponseEntity<List<User>> list() {
        return ResponseEntity.ok(userMapper.queryUserList());
    }
}
