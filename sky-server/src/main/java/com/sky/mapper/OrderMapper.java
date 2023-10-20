package com.sky.mapper;
import com.github.pagehelper.Page;
import com.sky.dto.GoodsSalesDTO;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.entity.Orders;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * @ClassName OrderMapper
 * @Description TODO
 * @Package com.sky.mapper
 * @Author Jia
 * @Date 2023/9/19 0019 22:10
 * @Version 17.0.7
 */
@Mapper
public interface OrderMapper {

    /*
    * 向订单表中插入一条数据
    * */
    void insert(Orders orders);

    /**
 * 分页条件查询并按下单时间排序
 * @param ordersPageQueryDTO
 */
    Page<Orders> pageQuery(OrdersPageQueryDTO ordersPageQueryDTO);


    /**
     * 根据订单号和用户id查询订单
     * @param orderNumber
     * @param userId
     */
    @Select("select * from orders where number = #{orderNumber} and user_id= #{userId}")
    Orders getByNumberAndUserId(String orderNumber, Long userId);

    /**
     * 修改订单信息
     * @param orders
     */
    void update(Orders orders);

    /**
     * 根据id查询订单数据
     */
    @Select("select * from orders where id=#{id}")
    Orders getById(Long id);

    /**
     * 根据状态统计订单数量
     * @param status
     */
    @Select("select count(id) from orders where status = #{status}")
    Integer countStatus(Integer status);

    //先要查询订单是待付款状态，然后时间要是小于当前时间-15分钟
    //select * from orders where status? and order_time=(now-15)
    @Select("select * from orders where status=#{status} and order_time<#{orderTime}")
    List<Orders> getByStatusAndOrderTimeTL(Integer status, LocalDateTime orderTime);

    /*
    *查询营业额数据
    *  */
//    @Select("select sum(amount) form orders where order_time> and order_time and status=#{}")
    Double sumByMap(Map map);

    /*
    * 根据动态条件来统计订单数量
    * */
    Integer countByMap(Map map);


    //统计销量全十
    List<GoodsSalesDTO> getSalesTop10(LocalDateTime begin, LocalDateTime end);
}
