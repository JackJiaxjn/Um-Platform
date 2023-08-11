package com.sky.controller.admin;

import com.sky.constant.JwtClaimsConstant;
import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.entity.Employee;
import com.sky.properties.JwtProperties;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.EmployeeService;
import com.sky.utils.JwtUtil;
import com.sky.vo.EmployeeLoginVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 员工管理
 */
@RestController
@RequestMapping("/admin/employee")
@Slf4j
@Api(tags = "员工相关接口") //用在类上，一般用在controller上，表示对类的说明
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private JwtProperties jwtProperties;

    /**
     * 登录
     *
     * @param employeeLoginDTO
     * @return
     */

    @PostMapping("/login")
    @ApiOperation(value = "员工登录") //用在controller层中的方法上对方法的说明,描述这个方法的具体业务功能
    public Result<EmployeeLoginVO> login(@RequestBody EmployeeLoginDTO employeeLoginDTO) {
        log.info("员工登录：{}", employeeLoginDTO);

        Employee employee = employeeService.login(employeeLoginDTO);

        //登录成功后，生成jwt令牌
        Map<String,Object> claims = new HashMap<>();
        claims.put(JwtClaimsConstant.EMP_ID, employee.getId());
        String token = JwtUtil.createJWT(
                jwtProperties.getAdminSecretKey(),
                jwtProperties.getAdminTtl(),
                claims);

        EmployeeLoginVO employeeLoginVO = EmployeeLoginVO.builder()
                .id(employee.getId())
                .userName(employee.getUsername())
                .name(employee.getName())
                .token(token)
                .build();

        return Result.success(employeeLoginVO);
    }

    /**
     * 退出
     *
     * @return
     */
    @PostMapping("/logout")
    @ApiOperation(value ="员工退出" ) //value可以省略。描述这个方法的具体作用，swagger的常用注解
    public Result<String> logout() {
        return Result.success();
    }

    /*
    * 新增员工
    * */
    @PostMapping
    @ApiOperation(value = "新增员工")
    //这里的增删改查都是响应的是json,且封装在employeeDTO类中
    public Result sava(@RequestBody EmployeeDTO employeeDTO){
        //记录一下输出日志
        log.info("新增员工: {}",employeeDTO);

        //输出一个当前线程的id,动态获取id，每次客户端发起一个请求都会创建一个单独的线程
        System.out.println("当前线程的id:"+Thread.currentThread().getId());

        //调用service层
        employeeService.save(employeeDTO);
        return Result.success();
    }

    /*
    * 员工表分页查询
    * */
    @GetMapping("/page")
    @ApiOperation("员工分页查询")
    public Result<PageResult> page(EmployeePageQueryDTO employeePageQueryDTO){
        log.info("分页查询员工,参数为: {}",employeePageQueryDTO);
        PageResult pageResult= employeeService.pageQuery(employeePageQueryDTO);
        return Result.success(pageResult);
    }

}
