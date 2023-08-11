package com.sky.annotation;

import com.sky.enumeration.OperationType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @ClassName AutoFill
 * @Description 自定义的注解，用于标识某个方法需要进行功能字段的填充处理
 * @Package com.sky.annotation
 * @Author Jia
 * @Date 2023/8/11 0011 21:07
 * @Version 17.0.7
 */
//这个注解用来指定只能用在方法上面
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AutoFill {
    //指定数据库的操作类型 update insert 只有在更新和插入的时候才有用
    OperationType value();
}
