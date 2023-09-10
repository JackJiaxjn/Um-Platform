package com.sky.mapper;

import com.sky.entity.DishFlavor;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @ClassName DishFlavorMapper
 * @Description
 * @Package com.sky.mapper
 * @Author Jia
 * @Date 2023/9/9 0009 15:28
 * @Version 17.0.7
 */
@Mapper
public interface DishFlavorMapper {

    /*
    * 批量插入口味数据
    * */
    void insertBatch(List<DishFlavor> flavors);
    /*
    *  //根据菜品id删除口味数据
    * */
    @Delete("delete from dish_flavor where dish_id=#{dishId}")
    void deleteByDishId(Long dishId);

    /*
    * 根据菜品id查询口味数据
    * */
    @Select("select * from dish_flavor where dish_id=#{dishId}")
    List<DishFlavor> getByDishId(Long dishId);
}
