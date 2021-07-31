package com.maying.springbootdemo.service;

import com.maying.springbootdemo.domain.entity.User;

/**
 * description
 */
public interface UserService {

    User login(String userName,String password);

    User update(User user);
}
