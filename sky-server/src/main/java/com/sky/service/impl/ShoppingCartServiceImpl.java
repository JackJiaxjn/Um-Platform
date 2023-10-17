package com.sky.service.impl;

import com.sky.context.BaseContext;
import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.Dish;
import com.sky.entity.Setmeal;
import com.sky.entity.ShoppingCart;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.mapper.ShoppingCartMapper;
import com.sky.service.ShoppingCartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;



/**
 * @ClassName ShoppingCardServiceImpl
 * @Description TODO
 * @Package com.sky.service.impl
 * @Author Jia
 * @Date 2023/9/19 0019 12:46
 * @Version 17.0.7
 */
@Service
@Slf4j
public class ShoppingCartServiceImpl implements ShoppingCartService {

    @Autowired
    private ShoppingCartMapper shoppingCartMapper;
    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private SetmealMapper setmealMapper;
    /*
     * 添加购物车
     * */
    @Override
    public void addShoppingCart(ShoppingCartDTO shoppingCartDTO) {
        //判断当前加入购物车中的商品是否已经存在
        ShoppingCart shoppingCart =new ShoppingCart();
        //属性拷贝,前面的拷贝到后面
        BeanUtils.copyProperties(shoppingCartDTO,shoppingCart);
        //获取用户id
        Long userId = BaseContext.getCurrentId();
        shoppingCart.setUserId(userId);
        //看下有没有查上来
        List<ShoppingCart> list = shoppingCartMapper.list(shoppingCart);

        //如果存在，只要将数量加一
        if(list!=null&&list.size()>0){
            //能查到也只要一条数据
            ShoppingCart shoppingCart1 = list.get(0);
            //把这条数据的数据修改一下加一
            shoppingCart1.setNumber(shoppingCart1.getNumber()+1);
            //然后再执行update语句，进行修改 update shopping_card set number =?  where id=?
            shoppingCartMapper.updateNumberById(shoppingCart1);
        }else{
            //如果不存在，需要插入一条购物车数据
            
            //先要判断添加购物车的是菜品还是套餐，然后再进行查询
            Long dishId = shoppingCartDTO.getDishId();
                if(dishId!=null){
                    //本次添加到购物车的是菜品,查询菜品数据，封装在购物车对象中
                    Dish dish = dishMapper.getById(dishId);
                    shoppingCart.setDishId(dish.getId());
                    shoppingCart.setImage(dish.getImage());
                    shoppingCart.setAmount(dish.getPrice());
                    shoppingCart.setName(dish.getName());

                }else{
                    //否则添加购物车的是套餐
                    Long setmealId = shoppingCartDTO.getSetmealId();
                    Setmeal setmeal = setmealMapper.getById(setmealId);
                    shoppingCart.setDishId(setmeal.getId());
                    shoppingCart.setImage(setmeal.getImage());
                    shoppingCart.setAmount(setmeal.getPrice());
                    shoppingCart.setName(setmeal.getName());

                }
                shoppingCart.setNumber(1);
                shoppingCart.setCreateTime(LocalDateTime.now());
                //统一插入数据
                shoppingCartMapper.insert(shoppingCart);

        }


    }
    /*
    * 查看购物车*/
    @Override
    public List<ShoppingCart> showShoppingCart() {
        //查询购物车中所有的数据
        //先获取当前微信用户的id
        Long userId = BaseContext.getCurrentId();
        ShoppingCart shoppingCart = ShoppingCart.builder()
                .userId(userId)
                .build();
        List<ShoppingCart> ShoppingCard = shoppingCartMapper.list(shoppingCart);
        return ShoppingCard;
    }

    /*
    * 清空购物车
    * */
    @Override
    public void delete() {
        Long userId = BaseContext.getCurrentId();
        shoppingCartMapper.delete(userId);
    }

    /**
     * 删除购物车中一个商品
     * @param shoppingCartDTO
     */
    @Override
    public void subShoppingCart(ShoppingCartDTO shoppingCartDTO) {

        ShoppingCart shoppingCart = new ShoppingCart();
        BeanUtils.copyProperties(shoppingCartDTO,shoppingCart);
        //设置查询条件，查询当前登录用户的购物车数据
        shoppingCart.setUserId(BaseContext.getCurrentId());

        List<ShoppingCart> list = shoppingCartMapper.list(shoppingCart);

        if(list != null && list.size() > 0){
            shoppingCart = list.get(0);

            Integer number = shoppingCart.getNumber();
            if(number == 1){
                //当前商品在购物车中的份数为1，直接删除当前记录
                shoppingCartMapper.deleteById(shoppingCart.getId());
            }else {
                //当前商品在购物车中的份数不为1，修改份数即可
                shoppingCart.setNumber(shoppingCart.getNumber() - 1);
                shoppingCartMapper.updateNumberById(shoppingCart);
            }
        }
    }


}
