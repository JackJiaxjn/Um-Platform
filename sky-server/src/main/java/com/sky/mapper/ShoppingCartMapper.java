package com.sky.mapper;

import com.sky.entity.ShoppingCart;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * @ClassName ShoppingCardMapper
 * @Description TODO
 * @Package com.sky.mapper
 * @Author Jia
 * @Date 2023/9/19 0019 13:02
 * @Version 17.0.7
 */
@Mapper
public interface ShoppingCartMapper {

    /*
    *查询购物车中的数据
    * */
    //select * from shopping_card where dish_id ? and dish_flavor
    //select * from shopping_card where user_id and
    //永动态条件查询   /*查询购物车中所有数据*/
    List<ShoppingCart> list(ShoppingCart shoppingCart);

    /*
    * 修改购物车商品数量
    * */
    @Update("update shopping_cart set number=#{number} where id=#{id} ")
    void updateNumberById(ShoppingCart shoppingCart);
    /*在购物车中插入数据*/
    @Insert("insert into shopping_cart(name,user_id,dish_id,setmeal_id,dish_flavor,number,amount,image,create_time)"+
    "values(#{name},#{userId},#{dishId},#{setmealId},#{dishFlavor},#{number},#{amount},#{image},#{createTime})")
    void insert(ShoppingCart shoppingCart);

    @Delete("delete from shopping_cart where user_id=#{userId} ")
    void delete( Long userId);


    /**
     * 根据id删除购物车数据
     * @param id
     */
    @Delete("delete from shopping_cart where id = #{id}")
    void deleteById(Long id);

    /*
    * 批量插入购物车数据
    * */
    void insertBatch(List<ShoppingCart> shoppingCartList);
}
