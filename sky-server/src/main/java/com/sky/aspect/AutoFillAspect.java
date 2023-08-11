package com.sky.aspect;

import com.sky.annotation.AutoFill;
import com.sky.constant.AutoFillConstant;
import com.sky.context.BaseContext;
import com.sky.enumeration.OperationType;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.time.LocalDateTime;


/**
 * @ClassName AutoFillAspect
 * @Description 自定义的切面类，用来统一拦截加入了AutoFill注解的方法
 * 切面就是切入点加上通知
 * @Package com.sky.aspect
 * @Author Jia
 * @Date 2023/8/11 0011 21:19
 * @Version 17.0.7
 */
//加入切面的注解
@Aspect
@Component
@Slf4j
public class AutoFillAspect {
    //定义切入点，对那些类的那些拦截

    //切入点指定的拦截包mapper和具体方法update insert
    @Pointcut("execution(* com.sky.mapper.*.*(..))&&@annotation(com.sky.annotation.AutoFill)")
    public void autoFillPointCut(){//切点表达式

    }

    /**
     * 用aop中的通知，前置通知，匹配切点表达式，在通知中对公共字段进行赋值
     */
    @Before("autoFillPointCut()")
    public void autoFill(JoinPoint joinPoint){//拦截的参数
        log.info("开始进行公共字段填充");

        //获取当前被拦截的方法上的数据库操作类型
        //向下转型成子接口
        MethodSignature signature = (MethodSignature)joinPoint.getSignature();//方法签名
        AutoFill autoFill = signature.getMethod().getAnnotation(AutoFill.class);//获得方法上的注解对象
        OperationType operationType = autoFill.value();//获得数据库操作类型

        //获取到当前被拦截的方法的参数--实体对象
        Object[] args = joinPoint.getArgs();
        //防止这个参数出现空指针，需要判断一下
        if(args==null||args.length==0){
            return;
        }
        //接收实体对象,默认获取第一个
        Object entity = args[0];

        //准备对实体对象公共的属性赋值,获取这里员工表插入数据的id和时间
        LocalDateTime now = LocalDateTime.now();
        Long currentId = BaseContext.getCurrentId();

        //根据当前不同的操作类型，为对应的属性通过反射来赋值
        if(operationType==OperationType.INSERT){
            //为四个公共字段赋值
                //通过反射获取set方法
            try {
                Method setCreateTime = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_CREATE_TIME, LocalDateTime.class);
                Method setCreateUser = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_CREATE_USER, Long.class);
                Method setUpdateTime = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_TIME, LocalDateTime.class);
                Method setUpdateUser = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_USER, Long.class);
                //通过反射为对象属性赋值
                setCreateTime.invoke(entity,now);
                setCreateUser.invoke(entity,currentId);
                setUpdateTime.invoke(entity,now);
                setUpdateUser.invoke(entity,currentId);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }


        }else if(operationType==OperationType.UPDATE){
            //为两个公共字段赋值
                //通过反射获取set方法
            try {
                Method setUpdateTime = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_TIME, LocalDateTime.class);
                Method setUpdateUser = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_USER, Long.class);
                //通过反射为对象属性赋值
                setUpdateTime.invoke(entity,now);
                setUpdateUser.invoke(entity,currentId);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

    }


}

























