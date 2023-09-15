package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.entity.Setmeal;
import com.sky.entity.SetmealDish;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.exception.SetmealEnableFailedException;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealDishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.SetmealService;
import com.sky.vo.DishItemVO;
import com.sky.vo.SetmealVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

/**
 * @ClassName SetmealServiceImpl
 * @Description TODO
 * @Package com.sky.service.impl
 * @Author Jia
 * @Date 2023/9/10 0010 18:17
 * @Version 17.0.7
 */
@Service
@Slf4j
public class SetmealServiceImpl implements SetmealService {

    @Autowired
    private SetmealMapper setmealMapper;
    @Autowired
    private SetmealDishMapper setmealDishMapper;
    @Autowired
    private DishMapper dishMapper;

    /*
     * 新增套餐和对应的菜品
     * */
    @Transactional
    @Override
    public void saveWithDish(SetmealDTO setmealDTO) {

        //先插入套餐数据
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO, setmeal);
        setmealMapper.save(setmeal);

        //获取返回的套餐id来插入菜品数据
        //获取insert语句生成的主键值,根据setmealI向菜品表中插入数据
        Long setmealId = setmeal.getId();
        //向菜品表中插入n条数据,在dto中取出菜品数据

        List<SetmealDish> setmealDishes = setmealDTO.getSetmealDishes();
        //判断菜品数据是否提交
        if (setmealDishes != null && setmealDishes.size() > 0) {

            //遍历一下setmealDishes这个集合
            setmealDishes.forEach(setmealDish -> {
                setmealDish.setSetmealId(setmealId);
            });

            //向菜品表中插入n条数据,批量插入数据
            setmealDishMapper.insertBatch(setmealDishes);
        }


    }

    /*
     * 套餐分页查询
     * */
    @Override
    public PageResult pageQuery(SetmealPageQueryDTO setmealPageQueryDTO) {
        PageHelper.startPage(setmealPageQueryDTO.getPage(), setmealPageQueryDTO.getPageSize());
        //自动分页查询出来结果
        Page<SetmealVO> page = setmealMapper.pageQuery(setmealPageQueryDTO);
        return new PageResult(page.getTotal(), page.getResult());
    }

    /*
     * 批量删除套餐
     * */
    @Override
    public void deleteBatch(List<Long> ids) {
        //判断当前套餐是否起售，如果是起售不能删除抛个异常
        ids.forEach(id -> {
            //遍历每个id查询的套餐数据
            Setmeal setmeal = setmealMapper.getById(id);
            //判断每个套餐数据是否在起售状态
            if (setmeal.getStatus() == StatusConstant.ENABLE) {
                //起售状态不能删除
                throw new DeletionNotAllowedException(MessageConstant.DISH_ON_SALE);
            }

        });

        //删除表中数据
        ids.forEach(setmealId -> {
            //删除套餐表中数据
            setmealMapper.deleteById(setmealId);
            //删除套餐菜品关系表中的数据
            setmealDishMapper.deleteBySetmealId(setmealId);
        });

    }

    /*
     * 根据套餐id查询套餐数据和相关联的菜品数据
     * */
    @Transactional
    @Override
    public SetmealVO getByIDWithDish(Long id) {
        //先查询套餐数据
        Setmeal setmeal = setmealMapper.getById(id);
        //查询关联菜品数据
        List<SetmealDish> setmealDishes = setmealDishMapper.getBySetmealId(id);

        SetmealVO setmealVO = new SetmealVO();
        BeanUtils.copyProperties(setmeal, setmealVO);
        setmealVO.setSetmealDishes(setmealDishes);
        return setmealVO;
    }

    /*
     *修改套餐和相关菜品
     * */
    @Transactional
    @Override
    public void update(SetmealDTO setmealDTO) {
        //先修改套餐数据
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO, setmeal);
        setmealMapper.update(setmeal);

        //套餐id
        Long setmealId = setmealDTO.getId();

        //根据套餐id删除相关联系的菜品数据
        setmealDishMapper.deleteBySetmealId(setmealId);

        //提取关联菜品数据
        List<SetmealDish> setmealDishes = setmealDTO.getSetmealDishes();

        //把获取的套餐id用循环一个一个插入到关联菜品的数据中
        setmealDishes.forEach(setmealDish -> {
            setmealDish.setSetmealId(setmealId);
        });
        //再整合,批量插入数据
        setmealDishMapper.insertBatch(setmealDishes);

    }

    /*
     * 起售停售套餐
     * */
    @Override
    @Transactional
    public void startOrStop(Integer status, Long id) {
        //起售套餐时，先判断套餐内是否有停售菜品，如果有停售菜品，要提示套餐内包含未起售菜品，无法起售
        if (status == StatusConstant.ENABLE) {
            //根据套餐id查询菜品数据，然后获取菜品的起售状态
            //select d.* from dish d left join setmeal_dish s on d.id = s.dish_id where s.setmeal_id=#{setmealId}
            List<Dish> disheList = dishMapper.getBySetmealId();
            if (disheList != null && disheList.size() > 0) {
                disheList.forEach(dish -> {
                    if (dish.getStatus() == StatusConstant.DISABLE) {
                        throw new SetmealEnableFailedException(MessageConstant.SETMEAL_ENABLE_FAILED);
                    }
                });
            }

        }

        //更新起售停售状态
        Setmeal setmeal = Setmeal.builder()
                .id(id)
                .status(status)
                .build();
        setmealMapper.update(setmeal);


    }


    /**
     * 条件查询
     * @param setmeal
     * @return
     */
    public List<Setmeal> list(Setmeal setmeal) {
        List<Setmeal> list = setmealMapper.list(setmeal);
        return list;
    }

    /**
     * 根据id查询菜品选项
     * @param id
     * @return
     */
    public List<DishItemVO> getDishItemById(Long id) {
        return setmealMapper.getDishItemBySetmealId(id);
    }

}
