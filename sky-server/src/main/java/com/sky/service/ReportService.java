package com.sky.service;
import com.sky.vo.OrderReportVO;
import com.sky.vo.SalesTop10ReportVO;
import com.sky.vo.TurnoverReportVO;
import com.sky.vo.UserReportVO;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;



/**
 * @ClassName ReportService
 * @Description TODO
 * @Package com.sky.service
 * @Author Jia
 * @Date 2023/10/19 0019 16:42
 * @Version 17.0.7
 */

public interface ReportService {

    /*
    * 统计给定时间区间内的营业额
    * */
    TurnoverReportVO getTurnoverStatistics(LocalDate begin, LocalDate end);

    /*
    * 统计用户数据
    * */
    UserReportVO getUserStatistics(LocalDate begin, LocalDate end);

    /*统计订单数据*/
    OrderReportVO getOrderStatistics(LocalDate begin, LocalDate end);

    /*
    * 销量排名全十*/
    SalesTop10ReportVO getSalesTop10(LocalDate begin, LocalDate end);

    /**
     * 导出运营数据报表
     * @param response
     */
    void exportBusinessData(HttpServletResponse response);
}
