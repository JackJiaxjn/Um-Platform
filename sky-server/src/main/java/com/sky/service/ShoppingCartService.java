package com.sky.service;

import com.sky.dto.ShoppingCartDTO;

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
}
