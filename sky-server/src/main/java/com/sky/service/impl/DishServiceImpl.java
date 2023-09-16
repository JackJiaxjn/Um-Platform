package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.entity.Employee;
import com.sky.entity.Setmeal;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.DishFlavorMapper;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealDishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.result.PageResult;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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
    @Autowired
    private SetmealDishMapper setmealDishMapper;
    @Autowired
    private SetmealMapper setmealMapper;
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

    /*
    * 菜品分页查询
    * */
    @Override
    public PageResult pageQuery(DishPageQueryDTO dishPageQueryDTO) {

            PageHelper.startPage(dishPageQueryDTO.getPage(),dishPageQueryDTO.getPageSize());//开始分页
            Page<DishVO> page = dishMapper.pageQuery(dishPageQueryDTO);
            long total = page.getTotal();
            List<DishVO> records = page.getResult();
            return new PageResult(total,records);
//        //分页查询select * from emp limit 0,10 maven中已经引入了分页查询的插件
//        //用插件开始分页查询,获取分页后的页数和页面大小
//        PageHelper.startPage(employeePageQueryDTO.getPage(),employeePageQueryDTO.getPageSize());
//        //这里会返回Page的对象
//        Page<Employee> page = employeeMapper.pageQuery(employeePageQueryDTO);
//        //这里page可以直接获取PageResult中的属性
//        long total = page.getTotal();//获取查询的页总数
//        List<Employee> records = page.getResult();//获取查询出的页面数据集合
//
//        return new PageResult(total,records);

    }
    /*
     * 菜品的批量删除
     **/
    @Override
    @Transactional
    public void deleteBatch(List<Long> ids) {
        //判断当前菜品是否能够删除----是否存在起售中的菜品？？
        for (Long id : ids) {//遍历一下每个ids数组，把每个id取出来，查询一下
            Dish dish = dishMapper.getById(id);
            if(dish.getStatus()== StatusConstant.ENABLE){
                //当前菜品处于起售中，不能删除,抛个异常
                throw new DeletionNotAllowedException(MessageConstant.DISH_ON_SALE);
            }
        }
        //判断当前菜品是否能够删除----是否存在套餐关联？？
        List<Long> setmealIds = setmealDishMapper.getSetmealIdsByDishIds(ids);
        if(setmealIds!=null&&setmealIds.size()>0){
            //查到了有关联套餐不能删除，抛个异常
            throw new DeletionNotAllowedException(MessageConstant.CATEGORY_BE_RELATED_BY_DISH);
        }
        //删除菜品表中的菜品数据
        for (Long id : ids) {
            dishMapper.deleteById(id);
            //删除菜品关联的口味
            dishFlavorMapper.deleteByDishId(id);
        }


    }

    /*
     * 根据id查询菜品
     * */
    @Override
    public DishVO getByIdWithFlavor(Long id) {

        //根据id查询菜品数据
        Dish dish = dishMapper.getById(id);

        //根据菜品id查询口味数据
        List<DishFlavor> dishFlavor = dishFlavorMapper.getByDishId(id);

        //将查询的数据封装到VO
        DishVO dishVO = new DishVO();
        //属性拷贝，把dish数据拷贝到dishVO中
        BeanUtils.copyProperties(dish,dishVO);
        dishVO.setFlavors(dishFlavor);
        return dishVO;
    }

    /*
     * 根据id修改菜品基本信息和对应的口味信息
     * */
    @Transactional
    @Override
    public void updateWithFlavor(DishDTO dishDTO) {

        //修改菜品表基本信息
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO,dish);
        dishMapper.update(dish);

        //这里要修改口味，先删除原先的口味数据
        List<DishFlavor> flavors = dishDTO.getFlavors();
        dishFlavorMapper.deleteByDishId(dishDTO.getId());

        // 然后批量再插入口味数据
        if(flavors!=null&&flavors.size()>0) {
            //遍历一下这个flavors集合
            flavors.forEach(dishFlavor -> {
                dishFlavor.setDishId(dishDTO.getId());
            });
        }
        dishFlavorMapper.insertBatch(flavors);


    }

    /*
     * 根据分类id查询菜品
     */
    @Override
    public List<Dish> list(Long categoryId) {

        Dish dish = Dish.builder()
                .categoryId(categoryId)
                .status(StatusConstant.ENABLE)
                .build();

        return dishMapper.list(dish);
    }

    /**
     * 条件查询菜品和口味
     * @param dish
     * @return
     */
    public List<DishVO> listWithFlavor(Dish dish) {
        List<Dish> dishList = dishMapper.list(dish);

        List<DishVO> dishVOList = new ArrayList<>();

        for (Dish d : dishList) {
            DishVO dishVO = new DishVO();
            BeanUtils.copyProperties(d,dishVO);

            //根据菜品id查询对应的口味
            List<DishFlavor> flavors = dishFlavorMapper.getByDishId(d.getId());

            dishVO.setFlavors(flavors);
            dishVOList.add(dishVO);
        }

        return dishVOList;
    }

    /**
     * 菜品起售停售
     *
     * @param status
     * @param id
     */
    @Transactional
    public void startOrStop(Integer status, Long id) {
        Dish dish = Dish.builder()
                .id(id)
                .status(status)
                .build();
        dishMapper.update(dish);

        if (status == StatusConstant.DISABLE) {
            // 如果是停售操作，还需要将包含当前菜品的套餐也停售
            List<Long> dishIds = new ArrayList<>();
            dishIds.add(id);
            // select setmeal_id from setmeal_dish where dish_id in (?,?,?)
            List<Long> setmealIds = setmealDishMapper.getSetmealIdsByDishIds(dishIds);
            if (setmealIds != null && setmealIds.size() > 0) {
                for (Long setmealId : setmealIds) {
                    Setmeal setmeal = Setmeal.builder()
                            .id(setmealId)
                            .status(StatusConstant.DISABLE)
                            .build();
                    setmealMapper.update(setmeal);
                }
            }
        }
    }

}
