package com.yiyayaya.shopmanage.controller;

import com.yiyayaya.shopmanage.common.Result;
import com.yiyayaya.shopmanage.exception.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
@RestController
@RequestMapping("/upload")
public class FileUploadController {

    private final String uploadDir = System.getProperty("user.dir") + "/src/main/resources/static/image/"; // 上传文件的目录

    @PostMapping
    public Result<String> uploadFile(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            throw new ValidationException("文件不能为空");
        }

        try {
            // 创建上传目录
            File dir = new File(uploadDir);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            // 获取文件后缀
            String originalFilename = file.getOriginalFilename();
            String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
            
            // 生成新的文件名
            String newFileName = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")) + fileExtension;
            File dest = new File(dir, newFileName);
            file.transferTo(dest);

            return Result.success(newFileName);
        } catch (IOException e) {
            return Result.error(404, "文件上传失败: " + e.getMessage());
        }
    }
} 