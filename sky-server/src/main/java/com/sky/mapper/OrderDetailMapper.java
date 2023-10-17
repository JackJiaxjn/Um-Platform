package com.sky.mapper;

import com.sky.entity.OrderDetail;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @ClassName OrderDetailMapper
 * @Description
 * @Package com.sky.mapper
 * @Author Jia
 * @Date 2023/9/19 0019 22:12
 * @Version 17.0.7
 */
@Mapper
public interface OrderDetailMapper {
    /*
    * 批量在订单明细表中插入数据
    * */
    void insertBatch(List<OrderDetail> orderDetailList);

    /*
    * 查询订单细节表
    * */
    @Select("select * from order_detail where order_id=#{orderId}")
    List<OrderDetail> getByOrderId(Long orderId);
}
