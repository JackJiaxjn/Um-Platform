package com.sky.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel(description = "员工登录时传递的数据模型")//用在实体类上的说明，用于描述这个类的作用
public class EmployeeLoginDTO implements Serializable {

    @ApiModelProperty("用户名")//这个注解用在实体类的属性上，描述属性的信息
    private String username;

    @ApiModelProperty("密码")
    private String password;

}
