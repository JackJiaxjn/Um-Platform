package com.sky.mapper;

import com.sky.entity.DishFlavor;
import org.apache.ibatis.annotations.Mapper;

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
}
