package com.sky.service;

import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.ShoppingCart;

import java.util.List;

/**
 * @ClassName ShoppingCardService
 * @Description
 * @Package com.sky.service
 * @Author Jia
 * @Date 2023/9/19 0019 12:44
 * @Version 17.0.7
 */
public interface ShoppingCartService {

   /*
   * 添加购物车
   * */
   void addShoppingCart(ShoppingCartDTO shoppingCartDTO);

   /*
   * 查看购物车
   * */
   List<ShoppingCart> showShoppingCart();

 /*清空购物车*/
   void delete();


    /**
     * 删除购物车中一个商品
     * @param shoppingCartDTO
     */
    void subShoppingCart(ShoppingCartDTO shoppingCartDTO);
}
