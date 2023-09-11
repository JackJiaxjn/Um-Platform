package com.sky.service;

import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.vo.SetmealVO;

import java.util.List;

/**
 * @ClassName SetmealService
 * @Description
 * @Package com.sky.service
 * @Author Jia
 * @Date 2023/9/10 0010 18:16
 * @Version 17.0.7
 */
public interface SetmealService {
    /*
    * 新增套餐和对应的菜品
    * */
    void saveWithDish(SetmealDTO setmealDTO);

    /*
    * 套餐分页查询
    * */
    PageResult pageQuery(SetmealPageQueryDTO setmealPageQueryDTO);

    /*
    * 批量删除套餐
    * */
    void deleteBatch(List<Long> ids);

    /*
    * 根据套餐id查询套餐数据和相关联的菜品数据
    * */
    SetmealVO getByIDWithDish(Long id);

    /*
    *修改套餐
    * */
    void update(SetmealDTO setmealDTO);

    /*
    * 起售停售套餐
    * */
    void startOrStop(Integer status, Long id);
}
