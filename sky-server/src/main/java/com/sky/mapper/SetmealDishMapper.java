package com.sky.mapper;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @ClassName SetmealDishMapper
 * @Description TODO
 * @Package com.sky.mapper
 * @Author Jia
 * @Date 2023/9/9 0009 21:17
 * @Version 17.0.7
 */
@Mapper
public interface SetmealDishMapper {

    /*
    根据菜品id来查询套餐id
    */
    //select setmeal_id from setmeal_dish where dish_id in(1,2,3,4,n)
    List<Long> getSetmealIdsByDishIds(List<Long> dishIds);
}
