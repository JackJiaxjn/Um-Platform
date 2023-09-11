package com.sky.controller.admin;

import com.sky.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.*;

/**
 * @ClassName ShopController
 * @Description 店铺状态
 * @Package com.sky.controller.admin
 * @Author Jia
 * @Date 2023/9/13 0013 20:44
 * @Version 17.0.7
 */
@RestController("adminShopController")
@RequestMapping("/admin/shop")
@Api(tags = "店铺相关接口")
@Slf4j
public class ShopController {

    public static final String KEY = "SHOP_STATUS";
    //注入redis配置类对象
    @Autowired
    private RedisTemplate redisTemplate;


    /*
    * 设置营业状态
    * */
    @PutMapping("/{status}")
    @ApiOperation("设置店铺营业状态")
    public Result setStatus(@PathVariable Integer status){
        log.info("设置店铺营业状态：{}",status==1? "营业中" : "打烊中");
        //使用redis中字符串存储类型来存储0/1两个状态
        ValueOperations valueOperations = redisTemplate.opsForValue();
        valueOperations.set(KEY,status);
        return Result.success();
    }

    /*
    * 获取店铺的营业状态
    * */
    @GetMapping("/status")
    @ApiOperation("获取店铺的营业状态")
    public Result<Integer> getStatus(){
        //使用redis中字符串存储类型来存储0/1两个状态
        ValueOperations valueOperations = redisTemplate.opsForValue();
        Integer shopStatus = (Integer) valueOperations.get(KEY);
        log.info("获取店铺营业状态：{}",shopStatus==1? "营业中" : "打烊中");
        return Result.success(shopStatus);
    }

















}
