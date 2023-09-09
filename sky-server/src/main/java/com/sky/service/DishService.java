package com.sky.service;

import com.sky.dto.DishDTO;

/**
 * @ClassName DishService
 * @Description
 * @Package com.sky.service
 * @Author Jia
 * @Date 2023/9/9 0009 14:23
 * @Version 17.0.7
 */
public interface DishService {

    /*
    * 新增菜品和对应的口味数据
    * */
    public void saveWithFlavor(DishDTO dishDTO);

}
