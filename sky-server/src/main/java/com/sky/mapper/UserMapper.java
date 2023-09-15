package com.sky.mapper;

import com.sky.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * @ClassName UserMapper
 * @Description TODO
 * @Package com.sky.mapper
 * @Author Jia
 * @Date 2023/9/15 0015 20:01
 * @Version 17.0.7
 */
@Mapper
public interface UserMapper {

    /*
    * 微信登录，根据openid来查用户
    * */
    @Select("select * from user where openid=#{openid}")
    User getByOpenid(String openid);

    /*
    * 如果是新用户到数据库中插入数据
    * */
    void insert(User user);
}
