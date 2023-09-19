package com.sky.controller.user;

import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.ShoppingCart;
import com.sky.result.Result;
import com.sky.service.ShoppingCartService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Delete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @ClassName ShopingCartController
 * @Description 添加购物车
 * @Package com.sky.controller.user
 * @Author Jia
 * @Date 2023/9/19 0019 12:25
 * @Version 17.0.7
 */
@RestController
@RequestMapping("user/shoppingCart")
@Slf4j
@Api(tags = "C端购物车相关接口")
public class ShoppingCartController {
    @Autowired
    private ShoppingCartService shoppingCardService;
    /*
    添加购物车
   */
    @ApiOperation("添加购物车")
    @PostMapping("/add")
    public Result add(@RequestBody ShoppingCartDTO shoppingCartDTO){
        log.info("添加购物车：商品信息为：{}",shoppingCartDTO);
        shoppingCardService.addShoppingCart(shoppingCartDTO);
        return Result.success();
    }

    /*
    *查看购物车
    * */
    @GetMapping("/list")
    @ApiOperation("查看购物车")
    public Result<List<ShoppingCart>> list(){
        List<ShoppingCart> list=shoppingCardService.showShoppingCart();

        return Result.success(list);
    }

    /*
     * 清空购物车
     */
    @DeleteMapping("/clean")
    @ApiOperation("清空购物车")
    public Result delete(){
        shoppingCardService.delete();
        return Result.success();
    }



}
