package com.sky.controller.user;
import com.sky.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import static com.sky.controller.admin.ShopController.KEY;

/**
 * @ClassName ShopController
 * @Description 店铺状态
 * @Package com.sky.controller.user
 * @Author Jia
 * @Date 2023/9/13 0013 21:26
 * @Version 17.0.7
 */
@RestController("userShopController")
@RequestMapping("/user/shop")
@Api(tags = "店铺相关接口")
@Slf4j
public class ShopController {

    //注入redis对象
    @Autowired
    private RedisTemplate redisTemplate;

    /*
    * 获取店铺的营业状态
    * */
    @GetMapping("/status")
    @ApiOperation("获取店铺的营业状态")
    public Result<Integer> getStatus(){
        Integer shopStatus =(Integer) redisTemplate.opsForValue().get(KEY);
        log.info("获取店铺的营业状态为：{}",shopStatus==1? "店铺营业" : "店铺打烊");
        return Result.success(shopStatus);

    }




}
