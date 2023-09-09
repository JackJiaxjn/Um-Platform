package com.sky.controller.admin;

import com.sky.constant.MessageConstant;
import com.sky.result.Result;
import com.sky.utils.AliOssUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.awt.font.MultipleMaster;
import java.io.IOException;
import java.util.UUID;

/**
 * @ClassName CommonController
 * @Description 通用接口
 * @Package com.sky.controller.admin
 * @Author Jia
 * @Date 2023/9/8 0008 19:36
 * @Version 17.0.7
 */
@RestController
@RequestMapping("/admin/common")
@Slf4j
@Api(tags="通用接口")
public class CommonController {
    /*
    * 文件上传
    * */

    //注入阿里云文件对象
    @Autowired
    private AliOssUtil aliOssUtil;


    @PostMapping("/upload")
    @ApiOperation("文件上传")
    public Result<String> upload(MultipartFile file){
        log.info("文件上传：{}",file);

        try {

            //获取原始文件名
            String originalFilename = file.getOriginalFilename();

            //截取原始文件名的后缀 dfdfdf.png 就是截取了扩展名
            String extension = originalFilename.substring(originalFilename.lastIndexOf("."));

            //用uuid加上这个扩展名，避免相同文件名上传时被覆盖,构成一个新的文件
            String objectName = UUID.randomUUID().toString() + extension;

            //把这个新的文件名通过upload方法拼接上，构成一个访问网址 filePath是请求路径
            String filePath = aliOssUtil.upload(file.getBytes(), objectName);
            return Result.success(filePath);
        } catch (IOException e) {
            log.info("文件上传失败：{}",e);
        }
        //调用定义好的常量文件上传失败
        return Result.error(MessageConstant.UPLOAD_FAILED);
    }
}
