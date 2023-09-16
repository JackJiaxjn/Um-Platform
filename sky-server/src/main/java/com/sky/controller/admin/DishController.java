package com.sky.controller.admin;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

/**
 * @ClassName DishController
 * @Description 菜品管理
 * @Package com.sky.controller.admin
 * @Author Jia
 * @Date 2023/9/9 0009 14:14
 * @Version 17.0.7
 */
@RestController
@RequestMapping("/admin/dish")
@Api(tags = "菜品相关管理")
@Slf4j
public class DishController {

    @Autowired
    private DishService dishService;
    @Autowired
    private RedisTemplate redisTemplate;

    /*
    * 新增菜品
    * */
    @PostMapping
    @ApiOperation("新增菜品")
    public Result sava(@RequestBody DishDTO dishDTO){
        log.info("新增菜品：{}",dishDTO);
        //新增菜品之后需要清理缓存数据
        //构造key,根据key来删除原来的数据
        String key = "dish_"+dishDTO.getCategoryId();
        redisTemplate.delete(key);

        dishService.saveWithFlavor(dishDTO);
        return Result.success();
    }

    /*
    * 菜品分页查询
    * */
    @GetMapping("/page")
    @ApiOperation("菜品分页查询")
    public Result<PageResult> page(DishPageQueryDTO dishPageQueryDTO){
        log.info("菜品分页查询：{}",dishPageQueryDTO);
        PageResult pageResult=dishService.pageQuery(dishPageQueryDTO);
        return Result.success(pageResult);

    }

    /*
    *批量删除菜品
    * */
    @DeleteMapping
    @ApiOperation("菜品批量删除")
    public Result delete(@RequestParam List<Long> ids){//@RequestParam是springmvc动态解析ids字符串，封装在list集合中
        log.info("菜品批量删除：{}",ids);
        dishService.deleteBatch(ids);

        //将所有的菜品的缓存数据全部清理掉，以dish_ 开头的key 先要查出dish_开头的数据，然后再删除
        cleanCache("dish_*");

        return Result.success();
    }

    /*
    * 根据id查询菜品
    * */
    @GetMapping("/{id}")
    @ApiOperation("根据id查询菜品")
    public Result<DishVO> getById(@PathVariable Long id){
        log.info("根据id查询菜品：{}",id);
        DishVO dishVO =dishService.getByIdWithFlavor(id);
        return Result.success(dishVO);
    }

    /*
    * 修改菜品
    * */
    @PutMapping
    @ApiOperation("修改菜品")
    public Result update(@RequestBody DishDTO dishDTO){
        log.info("修改菜品：{}",dishDTO);
        dishService.updateWithFlavor(dishDTO);

        //将所有的菜品的缓存数据全部清理掉，以dish_ 开头的key 先要查出dish_开头的数据，然后再删除
        cleanCache("dish_*");

        return Result.success();
    }

    /*
    * 根据分类id查询菜品
    * */
    @GetMapping("/list")
    @ApiOperation("根据分类id查询菜品")
    public Result<List<Dish>> list(Long categoryId){
        log.info("根据分类id查询菜品：{}",categoryId);
       List<Dish> list = dishService.list(categoryId);
        return Result.success(list);
    }

    /**
     * 菜品起售停售
     * @param status
     * @param id
     * @return
     */
    @PostMapping("/status/{status}")
    @ApiOperation("菜品起售停售")
    public Result<String> startOrStop(@PathVariable Integer status, Long id){
        dishService.startOrStop(status,id);

        //将所有的菜品的缓存数据全部清理掉，以dish_ 开头的key 先要查出dish_开头的数据，然后再删除
        cleanCache("dish_*");
        return Result.success();
    }

    //统一封装一个清理缓存的方法
    public void cleanCache(String pattern){
        //将所有的菜品的缓存数据全部清理掉，以dish_ 开头的key 先要查出dish_开头的数据，然后再删除
        Set keys = redisTemplate.keys(pattern);
        redisTemplate.delete(keys);
    }




}
