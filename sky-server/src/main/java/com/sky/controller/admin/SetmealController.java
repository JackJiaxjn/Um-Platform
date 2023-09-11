package com.sky.controller.admin;

import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.SetmealService;
import com.sky.vo.SetmealVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * @ClassName SetmealController
 * @Description 套餐管理
 * @Package com.sky.controller.admin
 * @Author Jia
 * @Date 2023/9/10 0010 18:02
 * @Version 17.0.7
 */
@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
@RestController
@RequestMapping("/admin/setmeal")
@Slf4j
@Api(tags = "套餐相关管理")
public class SetmealController {

    @Autowired
    private SetmealService setmealService;

    /*
    * 新增套餐
    * */
    @PostMapping
    @ApiOperation("新增套餐")
    public Result sava(@RequestBody SetmealDTO setmealDTO){
        log.info("新增套餐：{}",setmealDTO);
        setmealService.saveWithDish(setmealDTO);
        return Result.success();
    }

    /*
     * 套餐分页查询
     * */
    @GetMapping("/page")
    @ApiOperation("分页查询")
    public Result<PageResult> page(SetmealPageQueryDTO setmealPageQueryDTO){
        log.info("套餐分页查询：{}",setmealPageQueryDTO);
        PageResult pageResult = setmealService.pageQuery(setmealPageQueryDTO);
        return Result.success(pageResult);
    }

    /*
    * 根据ids批量删除套餐
    * */
    @DeleteMapping
    @ApiOperation("批量删除套餐")
    public Result delete(@RequestParam List<Long> ids){
        log.info("根据id批量删除套餐：{}",ids);
        setmealService.deleteBatch(ids);
        return Result.success();
    }

    /*
    * 根据id查询套餐
    * */
    @GetMapping("/{id}")
    @ApiOperation("根据id查询套餐")
    public Result<SetmealVO> getById(@PathVariable Long id){
        log.info("根据id查询套餐：{}",id);
        SetmealVO setmealVO = setmealService.getByIDWithDish(id);
        return Result.success(setmealVO);
    }

    /*
    *修改套餐
    * */
    @PutMapping
    @ApiOperation("修改套餐")
    public Result update(@RequestBody SetmealDTO setmealDTO){
        log.info("修改套餐：{}",setmealDTO);
        setmealService.update(setmealDTO);
        return Result.success();
    }

    /*
    * 套餐起售 停售
    * */
    @PostMapping("/status/{status}")
    @ApiOperation("套餐起售或停售")
    public Result startOrStop(@PathVariable Integer status,Long id){
        setmealService.startOrStop(status,id);
        return Result.success();
    }


















































}
