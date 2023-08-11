package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.PasswordConstant;
import com.sky.constant.StatusConstant;
import com.sky.context.BaseContext;
import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.entity.Employee;
import com.sky.exception.AccountLockedException;
import com.sky.exception.AccountNotFoundException;
import com.sky.exception.PasswordErrorException;
import com.sky.mapper.EmployeeMapper;
import com.sky.result.PageResult;
import com.sky.service.EmployeeService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeMapper employeeMapper;

    /**
     * 员工登录
     *
     * @param employeeLoginDTO
     * @return
     */
    public Employee login(EmployeeLoginDTO employeeLoginDTO) {
        String username = employeeLoginDTO.getUsername();

        //这个明文密码需要修改，用md5摘要算法
        String password = employeeLoginDTO.getPassword();

        //1、根据用户名查询数据库中的数据
        Employee employee = employeeMapper.getByUsername(username);

        //2、处理各种异常情况（用户名不存在、密码不对、账号被锁定）
        if (employee == null) {
            //账号不存在
            throw new AccountNotFoundException(MessageConstant.ACCOUNT_NOT_FOUND);
        }

        //密码比对
        //  对前端传过来的明文进行md5加密，然后再进行比对 代码如下
         password = DigestUtils.md5DigestAsHex(password.getBytes());
        if (!password.equals(employee.getPassword())) {
            //密码错误
            throw new PasswordErrorException(MessageConstant.PASSWORD_ERROR);
        }

        if (employee.getStatus() == StatusConstant.DISABLE) {
            //账号被锁定
            throw new AccountLockedException(MessageConstant.ACCOUNT_LOCKED);
        }

        //3、返回实体对象
        return employee;
    }
   /*
   * 实现新增员工信息的方法
   * */
    @Override
    public void save(EmployeeDTO employeeDTO) {

        //输出一个当前线程的id,动态获取id
        System.out.println("当前线程的id:"+Thread.currentThread().getId());


        //这里EmployeeDTO是封装类，最终要实现还要转换成实体类Employee
        //也就是对象属性拷贝,从employeeDTO对象属性拷贝给employee
        Employee employee = new Employee();
        BeanUtils.copyProperties(employeeDTO,employee);
        //employee中的有些属性employeeDTO没有，所以要单独设置

        //设置账号状态，默认账号是正常状态 1表示正常 0表示锁定
        employee.setStatus(StatusConstant.ENABLE);//这里填已经定义的常量类

        //设置密码，默认密码13456，密码需要加密，要用到DigestUtils.md5DigestAsHex("密码“.getBytes())
        employee.setPassword(DigestUtils.md5DigestAsHex(PasswordConstant.DEFAULT_PASSWORD.getBytes())); //也是调用已经设置好的常量密码

        //设置当前记录的创建时间和修改时间
        employee.setCreateTime(LocalDateTime.now());
        employee.setUpdateTime(LocalDateTime.now());

        //设置当前记录创建人id和修改人id
        //通过工具类在拦截器中获取的empId
        employee.setCreateUser(BaseContext.getCurrentId());
        employee.setUpdateUser(BaseContext.getCurrentId());

        //调用查询的方法
        employeeMapper.insert(employee);

    }
    /*
    * 员工分页查询实现方法
    * */
    @Override
    public PageResult pageQuery(EmployeePageQueryDTO employeePageQueryDTO) {
        //分页查询select * from emp limit 0,10 maven中已经引入了分页查询的插件
        //用插件开始分页查询,获取分页后的页数和页面大小
        PageHelper.startPage(employeePageQueryDTO.getPage(),employeePageQueryDTO.getPageSize());
        //这里会返回Page的对象
        Page<Employee> page = employeeMapper.pageQuery(employeePageQueryDTO);
        //这里page可以直接获取PageResult中的属性
        long total = page.getTotal();//获取查询的页总数
        List<Employee> records = page.getResult();//获取查询出的页面数据集合

        return new PageResult(total,records);
    }

    /**
     * 启用禁用员工账号
     */
    @Override
    public void startOrStop(Integer status, Long id) {
        //实现修改数据库表中的状态,根据id来
        //update employee set status=? where id =?

        /*方式一直接封装在employ对象中2
        Employee employee = new Employee();
        employee.setStatus(status);
        employee.setId(id);*/

        /*
        方式二，通过构建器直接设置
        */
        Employee employee = Employee.builder()
                .status(status)
                .id(id)
                .build();
        employeeMapper.update(employee);
    }
}
