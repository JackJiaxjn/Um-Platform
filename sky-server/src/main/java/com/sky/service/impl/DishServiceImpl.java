package com.sky.service.impl;

import com.sky.dto.DishDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.mapper.DishFlavorMapper;
import com.sky.mapper.DishMapper;
import com.sky.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @ClassName DishServicelmpl
 * @Description 菜品相关接口的实现类
 * @Package com.sky.service.impl
 * @Author Jia
 * @Date 2023/9/9 0009 14:27
 * @Version 17.0.7
 */
@Service
@Slf4j
public class DishServiceImpl implements DishService {

    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private DishFlavorMapper dishFlavorMapper;
    /*
     * 新增菜品和对应的口味数据
     * */
    @Override
    @Transactional //事务注解，这里操作两张表，保证数据的一致性
    public void saveWithFlavor(DishDTO dishDTO) {

        Dish dish = new Dish();
        //属性拷贝
        BeanUtils.copyProperties(dishDTO,dish);
        //向菜品表中插入一条数据
        dishMapper.insert(dish); //这里只需要插入菜品即可

        //获取insert语句生成的主键值,就能在口味数据的dishId插入
        Long dishId = dish.getId();

        //向口味表中插入n条数据,在dto中取出口味数据
        List<DishFlavor> flavors = dishDTO.getFlavors();
        //判断口味数据是否提交
        if(flavors!=null&&flavors.size()>0){

            //遍历一下这个flavors集合
            flavors.forEach(dishFlavor -> {
                dishFlavor.setDishId(dishId);
            });

            //向口味表中插入n条数据,批量插入数据
            dishFlavorMapper.insertBatch(flavors);

        }


    }




}
