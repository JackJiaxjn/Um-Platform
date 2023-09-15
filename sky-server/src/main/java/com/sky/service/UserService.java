package com.sky.service;

import com.sky.dto.UserLoginDTO;
import com.sky.entity.User;

/**
 * @ClassName UserService
 * @Description
 * @Package com.sky.service
 * @Author Jia
 * @Date 2023/9/15 0015 19:09
 * @Version 17.0.7
 */
public interface UserService {


    /*
     * 微信登陆
     * */
    User wxLogin(UserLoginDTO userLoginDTO);


}
