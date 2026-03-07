package com.cqz.beverage.controller;

import com.cqz.beverage.exception.BusinessExceptionEnum;
import com.cqz.beverage.exception.Result;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/common")
public class CommonController {

    // 建议在 application.yml 中配置此路径
    @Value("${file.upload-path:C:/uploads/}")
    private String uploadPath;

    @PostMapping("/upload")
    public Result<String> upload(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return Result.fail(BusinessExceptionEnum.PARAM_EMPTY.getCode(), "文件不能为空");
        }

        try {
            // 1. 生成唯一文件名，防止重名覆盖
            String originalFilename = file.getOriginalFilename();
            String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
            String fileName = UUID.randomUUID().toString() + suffix;

            // 2. 确定保存目录
            File destDir = new File(uploadPath);
            if (!destDir.exists()) destDir.mkdirs();

            // 3. 保存文件到本地磁盘
            File destFile = new File(destDir, fileName);
            file.transferTo(destFile);

            // 4. 返回文件的访问 URL
            // 注意：这里需要结合下方的静态资源映射配置
            String fileUrl = "/uploads/" + fileName; 
            return Result.success(fileUrl);

        } catch (IOException e) {
            e.printStackTrace();
            return Result.fail(BusinessExceptionEnum.SYSTEM_ERROR.getCode(), "文件上传失败");
        }
    }
}