package com.sky.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sky.constant.MessageConstant;
import com.sky.dto.UserLoginDTO;
import com.sky.entity.User;
import com.sky.exception.LoginFailedException;
import com.sky.mapper.UserMapper;
import com.sky.properties.WeChatProperties;
import com.sky.service.UserService;
import com.sky.utils.HttpClientUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName UserServiceImpl
 * @Description TODO
 * @Package com.sky.service.impl
 * @Author Jia
 * @Date 2023/9/15 0015 19:35
 * @Version 17.0.7
 */
@Service
@Slf4j
public class UserServiceImpl implements UserService {

    //微信服务接口地址
    public static final String WX_LOGIN = "https://api.weixin.qq.com/sns/jscode2session";
    //注入微信openid secret 配置属性以便于调用
    @Autowired
    private WeChatProperties weChatProperties;

    //注入mapper
    @Autowired
    private UserMapper userMapper;


    /*
    * 微信登录
    * */
    @Override
    public User wxLogin(UserLoginDTO userLoginDTO) {

        //调用微信接口服务，获得当前微信用户的openid 用httpclient
        String openid = getOpenid(userLoginDTO.getCode());

        //判断openid是否为空，如果为空表示登录失败，抛出异常
        if(openid==null){
            throw new LoginFailedException(MessageConstant.LOGIN_FAILED);
        }
        //判断当前用户是否为新用户,要去查用户表中有没有openid
        User user = userMapper.getByOpenid(openid);
        if(user==null){
            //如果是新用户，自动完成注册
            user=User.builder()
                    .openid(openid)
                    .createTime(LocalDateTime.now())
                    .build();
            userMapper.insert(user);

        }
        //返回用户对象
        return user;
    }

    /*
    * 调用微信接口服务，获取微信用户的openid
    * */
    private String getOpenid(String code){
        //调用微信接口服务，获得当前微信用户的openid 用httpclient
        Map<String, String> map= new HashMap<>();
        map.put("appid",weChatProperties.getAppid());
        map.put("secret",weChatProperties.getSecret());
        map.put("js_code",code);
        map.put("grant_type","authorization_code");
        String json = HttpClientUtil.doGet(WX_LOGIN, map);

        //获取openid
        JSONObject jsonObject = JSON.parseObject(json);
        String openid = jsonObject.getString("openid");
        return openid;
    }




}
