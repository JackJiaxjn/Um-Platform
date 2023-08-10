package com.sky.handler;

import com.sky.constant.MessageConstant;
import com.sky.exception.BaseException;
import com.sky.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.message.Message;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLIntegrityConstraintViolationException;

/**
 * 全局异常处理器，处理项目中抛出的业务异常
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * 捕获业务异常
     * @param ex
     * @return
     */
    @ExceptionHandler
    public Result exceptionHandler(BaseException ex){
        log.error("异常信息：{}", ex.getMessage());
        return Result.error(ex.getMessage());
    }

    /*
    * 处理sql异常,这个异常是姓名不能重复的异常
    * */
    @ExceptionHandler
    public Result exceptionHandler(SQLIntegrityConstraintViolationException ex){
            //异常信息为 Duplicate entry 'zhangqoq' for key 'employee.idx_username'
        //获取这条异常信息，然后再判断这个异常信息是否属于这个异常信息的字符串
        String message = ex.getMessage();
        if(message.contains("Duplicate entry")){
            //再把这条信息拆解成数组，字符串拆成字符串
            String[] split = message.split("");
            //再找出这条信息的用户名,下面已经找出
            String username = split[2];
            //再动态的把这条异常信息拼接上去
            String msg = username+ MessageConstant.ALREADY_EXISTS;//调用常量类，说明这个username已经存在
            //然后再返回这个结果
            return Result.error(msg);
        }else{
            //未知错误
            return Result.error(MessageConstant.UNKNOWN_ERROR);
        }

    }





}
