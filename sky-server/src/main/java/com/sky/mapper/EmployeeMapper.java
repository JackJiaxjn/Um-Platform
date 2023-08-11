package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.annotation.AutoFill;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.entity.Employee;
import com.sky.enumeration.OperationType;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface EmployeeMapper {

    /**
     * 根据用户名查询员工
     * @param username
     * @return
     */
    @Select("select * from employee where username = #{username}")
    Employee getByUsername(String username);


    /*
    * 插入员工数据
    * */
    @Insert("insert into employee(name, username, password, phone, sex, id_number,status, create_time, update_time, create_user, update_user)"+
            "values"+
            "(#{name},#{username},#{password},#{phone},#{sex},#{idNumber},#{status},#{createTime},#{updateTime},#{createUser},#{updateUser})"
    )
    //进行公共字段的填充，加入已经自己好的注解,指定是插入操作的填充
    @AutoFill(value = OperationType.INSERT)
    void insert(Employee employee);


    /**
     * 分页查询
     * @return
     */
    //动态查询用映射文件来写sql语句
    Page<Employee> pageQuery(EmployeePageQueryDTO employeePageQueryDTO);

    /**
     * 启用和禁用员工 账号
     */
    //动态修改需要在mapper映射文件中写sql语句
    //进行公共字段的填充，加入已经自己好的注解,指定是插入跟新的填充
    @AutoFill(value = OperationType.UPDATE)
    void update(Employee employee);


    /**
     * 根据id查询员工
     */
    @Select("select * from employee where id=#{id}")
    Employee getById(Long id);

}
