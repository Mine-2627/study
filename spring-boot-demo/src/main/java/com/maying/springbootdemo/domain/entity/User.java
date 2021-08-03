package com.maying.springbootdemo.domain.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 用户信息表
 */
@ApiModel("用户")
@Data
public class User {
    @ApiModelProperty("ID")
    private Long userId;
    @ApiModelProperty("用户姓名")
    private String userName;
    @ApiModelProperty("密码")
    private String password;
    @ApiModelProperty("生日")
    @JsonFormat(pattern = "yyyy-MM-dd HH-mm-ss")
    private Date birthday;
    @ApiModelProperty("手机号码")
    private String phoneNumber;
    @ApiModelProperty("邮箱")
    private String email;

}
