package com.sky.service;

import com.sky.dto.*;
import com.sky.result.PageResult;
import com.sky.vo.OrderPaymentVO;
import com.sky.vo.OrderStatisticsVO;
import com.sky.vo.OrderSubmitVO;
import com.sky.vo.OrderVO;

/**
 * @ClassName OrderService
 * @Description TODO
 * @Package com.sky.service
 * @Author Jia
 * @Date 2023/9/19 0019 22:03
 * @Version 17.0.7
 */
public interface OrderService {

    /*用户下单*/
    OrderSubmitVO submitOrder(OrdersSubmitDTO ordersSubmitDTO);

    /**
     * 订单支付
     * @param ordersPaymentDTO
     * @return
     */
    OrderPaymentVO payment(OrdersPaymentDTO ordersPaymentDTO) throws Exception;

    /**
     * 支付成功，修改订单状态
     * @param outTradeNo
     */
    void paySuccess(String outTradeNo);


    /*
    * 分页查询历史订单
    * */
    PageResult pageQuery4User(int page, int pageSize, Integer status);

/**
 * 查询订单详情
 */
    OrderVO details(Long id);

    /*
    * 取消订单*/
    void userCancelById(Long id) throws Exception;

    /*
    * 再来一单
    * */
    void repetition(Long id);


    /*
    * 订单搜索功能
   */
    PageResult conditionSearch(OrdersPageQueryDTO ordersPageQueryDTO);


    /*
    * 各个状态的订单数量统计
    * */
    OrderStatisticsVO statistics();

    /*
    * 接单
    * */
    void confirm(OrdersConfirmDTO ordersConfirmDTO);

    /*
    * 拒单
    * */
    void rejection(OrdersRejectionDTO ordersRejectionDTO) throws Exception;

    /**
     * 商家取消订单
     *
     * @param ordersCancelDTO
     */
    void cancel(OrdersCancelDTO ordersCancelDTO) throws Exception;

    /**
     * 派送订单
     *
     * @param id
     */
    void delivery(Long id);

    /**
     * 完成订单
     *
     * @param id
     */
    void complete(Long id);

    /*
    * 用户催单
    * */
    void reminder(Long id);
}
