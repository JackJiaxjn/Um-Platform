package com.sky.Task;

import com.sky.entity.Orders;
import com.sky.mapper.OrderMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @ClassName OrderTask
 * @Description 定时任务类
 * @Package com.sky.Task
 * @Author Jia
 * @Date 2023/10/17 0017 16:00
 * @Version 17.0.7
 */
//定时任务类，来处理订单状态
@Component
@Slf4j
public class OrderTask {

    @Autowired
    private OrderMapper orderMapper;
    /*
    * 处理超时订单的方法
    * */
    @Scheduled(cron = "0 * * * * ?")//每隔一分钟触发一次
    public void processTimeOut(){
        log.info("定时处理超时订单；{}", LocalDateTime.now());

        //先要查询订单是待付款状态，然后时间要是当前时间-15分钟
        //select * from orders where status? and order_time=(now-15)
        LocalDateTime time = LocalDateTime.now().plusMinutes(-15);//当前时间往前推15分钟
        List<Orders> orderList = orderMapper.getByStatusAndOrderTimeTL(Orders.PENDING_PAYMENT, time);

        //判断一下查询的数据，遍历一下查到的数据
        if(orderList.size()>0&&orderList!=null){
            //遍历这个集合把订单状态修改为取消
            for (Orders orders : orderList) {
                orders.setStatus(Orders.CANCELLED);
                orders.setCancelReason("订单超时，自动取消");
                orders.setOrderTime(LocalDateTime.now());
                //修改后跟新一下
                orderMapper.update(orders);
            }
        }
    }

    /*
    * 处理一直派送中的订单
    * */
    @Scheduled(cron = "0 0 1 * * ?")//24小时触发一次，每天凌晨1点触发
    public void processDeliveryOrder(){
        log.info("处理一直派送中的订单；{}", LocalDateTime.now());
        //查询处于派送中的订单，然后进行完成订单
        //当前时间减去1个小时
        LocalDateTime time = LocalDateTime.now().plusMinutes(-60);
        List<Orders> ordersList = orderMapper.getByStatusAndOrderTimeTL(Orders.DELIVERY_IN_PROGRESS, time);

        if(ordersList!=null&&ordersList.size()>0) {
            for (Orders orders : ordersList) {
                orders.setStatus(Orders.COMPLETED);//订单完成
                orderMapper.update(orders);
            }

        }
    }

}
