package com.maying.springbootdemo.infra.mapper;

import com.maying.springbootdemo.domain.entity.User;
;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 用户信息mapper
 */
@Mapper
@Repository
public interface UserMapper {

    /**
     * 查询用户列表
     *
     * @return List<User>
     */
    List<User> queryUserList();

    User queryUserId(Long userId);

    int insertUser(User user);

    int updateUser(User user);
}
