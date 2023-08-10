package com.sky.dto;

import lombok.Data;

import java.io.Serializable;

@Data
//实体类的基本用法
public class EmployeeDTO implements Serializable {

    private Long id;

    private String username;

    private String name;

    private String phone;

    private String sex;

    private String idNumber;

}
