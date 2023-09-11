package com.sky.mapper;

import com.sky.entity.SetmealDish;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

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

    /*
    * 批量插入菜品数据
    **/
    void insertBatch(List<SetmealDish> setmealDishes);

    /*
    * 根据套餐id删除套餐和菜品的关联关系
    **/
    @Delete("delete from setmeal_dish where setmeal_id=#{setmealId}")
    void deleteBySetmealId(Long setmealId);


    /*
    * 根据套餐id查询菜品关联的数据
    * */
    @Select("select * from setmeal_dish where setmeal_id=#{setmealId}")
    List<SetmealDish> getBySetmealId(Long id);
}
