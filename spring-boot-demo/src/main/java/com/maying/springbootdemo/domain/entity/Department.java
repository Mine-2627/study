package com.maying.springbootdemo.domain.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * description
 */
@ApiModel("部门")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Department {

    @ApiModelProperty("部门id")
    private Long departmentId;

    @ApiModelProperty("部门名称")
    private String departmentName;

    @ApiModelProperty("父级部门id")
    private Long parentId;

    @ApiModelProperty("部门编码")
    private String departmentNum;

}
