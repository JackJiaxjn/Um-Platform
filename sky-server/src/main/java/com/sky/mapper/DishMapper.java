package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.annotation.AutoFill;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.enumeration.OperationType;
import com.sky.vo.DishVO;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

@Mapper
public interface DishMapper {

    /**
     * 根据分类id查询菜品数量
     * @param categoryId
     * @return
     */
    @Select("select count(id) from dish where category_id = #{categoryId}")
    Integer countByCategoryId(Long categoryId);

    /*
    * 插入菜品数据
    * */
    //已经开发好了公共字段填充
    @AutoFill(value = OperationType.INSERT)
    void insert(Dish dish);

    /*
    * 菜品分页查询
    * */
    Page<DishVO> pageQuery(DishPageQueryDTO dishPageQueryDTO);

    /*
    * 根据主键查询菜品
    * */
    @Select("select * from dish where id=#{id}")
    Dish getById(Long id);

    /*
    * 根据主键删除菜品id
    * */
    @Delete("delete from dish where id=#{id} ")
    void deleteById(Long id);

    /*
     *根据id来动态修改菜品
     * 动态sql去删除
     */
    @AutoFill(value=OperationType.UPDATE)//公共字段自动填充
    void update(Dish dish);

    /*
    * 动态条件查询菜品
    * */
    List<Dish> list(Dish dish);

    /*
    * 根据套餐id查询菜品
    * */
    @Select("select d.* from dish d left join setmeal_dish sd on d.id = sd.dish_id where sd.setmeal_id=#{setmealId}")
    List<Dish> getBySetmealId();

    /**
     * 根据条件统计菜品数量
     * @param map
     * @return
     */
    Integer countByMap(Map map);
}
