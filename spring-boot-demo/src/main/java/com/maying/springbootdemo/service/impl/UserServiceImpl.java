package com.maying.springbootdemo.service.impl;

import com.maying.springbootdemo.controller.UserController;
import com.maying.springbootdemo.domain.entity.User;
import com.maying.springbootdemo.service.UserService;
import org.junit.Test;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * description
 */
public class UserServiceImpl implements UserService {

    @Override
    public User login(String userName, String password) {
        return null;
    }

    @Override
    public User update(User user) {
        return null;
    }


    public static void main(String[] args) {
        List<User> userList = new ArrayList<>();
        User user = new User();
        user.setUserId(1L);
        user.setParentId(0L);
        user.setAge(1);
        userList.add(user);
        User user2 = new User();
        user2.setUserId(2L);
        user2.setParentId(1L);
        user2.setAge(2);
        userList.add(user2);
        User user3 = new User();
        user3.setUserId(3L);
        user3.setParentId(2L);
        user3.setLeaf(1);
        user3.setAge(3);
        userList.add(user3);
        User user4 = new User();
        user4.setUserId(4L);
        user4.setParentId(2L);
        user4.setLeaf(1);
        user4.setAge(4);
        userList.add(user4);



        Map<Long,User> resultMap = userList.stream().collect(Collectors.toMap(User::getUserId,a->a));

        //对树分层
        int max = 1;
        int level = 1;
        for(User planDetail:userList){
            User temp = planDetail;
            while (temp.getParentId()!=0L){
                temp = resultMap.get(temp.getParentId());
                level++;
            }
            planDetail.setNodeLevel(level);
            max = max>level?max:level;
            level = 1 ;
        }
        //从最底层的树往上求和
        Map<Integer,List<User>> levelMap =userList.stream().collect(Collectors.groupingBy(User::getNodeLevel));
        for(Integer i = max; i>1;i--){
            List<User> levelList = levelMap.get(i);
            for(User sonNode :levelList){
                User parentNode = resultMap.get(sonNode.getParentId());
                parentNode.setAge(sonNode.getAge()+parentNode.getAge());
            }

        }




        System.out.println(userList.toString());
    }



}
