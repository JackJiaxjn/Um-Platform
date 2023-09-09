package com.sky.config;

import com.sky.properties.AliOssProperties;
import com.sky.utils.AliOssUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @ClassName OssConfiguration
 * @Description 配置类，用来创建AliOssUtil对象
 * @Package com.sky.config
 * @Author Jia
 * @Date 2023/9/8 0008 19:53
 * @Version 17.0.7
 */
@Configuration
@Slf4j
public class OssConfiguration {


    //创建一个方法来读取配置文件，对AliOssUtil对象属性赋值
    @Bean//加bean注解，项目启动的时候就会调用到这个方法
    @ConditionalOnMissingBean//保证spring容器中只有aliOssUtil一个对象
    public AliOssUtil aliOssUtil (AliOssProperties aliOssProperties){

        log.info("开始创建阿里云文件上传工具类对象：{}",aliOssProperties);

       return new AliOssUtil(aliOssProperties.getEndpoint(),
                aliOssProperties.getAccessKeyId(),
                aliOssProperties.getAccessKeySecret(),
                aliOssProperties.getBucketName());

    }

}
