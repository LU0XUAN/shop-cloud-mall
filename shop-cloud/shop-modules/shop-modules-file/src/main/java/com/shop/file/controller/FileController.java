package com.shop.file.controller;

import com.shop.core.domain.R;
import com.shop.core.domain.model.SysFile;
import com.shop.security.annotation.Log;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

@RestController
public class FileController {

    @Value("${file.windows.path}")
    private String windowsPath;

    @Value("${file.mac.path}")
    private String macPath;

    @Value("${file.linux.path}")
    private String linuxPath;

    /**
     * 文件上传
     */
    @PostMapping("/upload")
    @Log(describe = "文件上传")
    public R<?> upload(@RequestParam("file") MultipartFile file, HttpServletRequest request) {
        if (file.isEmpty()) {
            return R.fail("上传文件不能为空");
        }

        // 获取文件名
        String fileName = file.getOriginalFilename();
        // 获取文件后缀
        String suffixName = fileName.substring(fileName.lastIndexOf("."));
        // 生成新文件名
        String newFileName = UUID.randomUUID() + suffixName;

        // 获取系统类型，选择存储路径
        String os = System.getProperty("os.name");
        String filePath;
        if (os.toLowerCase().startsWith("win")) {
            filePath = windowsPath;
        } else if (os.toLowerCase().startsWith("mac")) {
            filePath = macPath;
        } else {
            filePath = linuxPath;
        }

        // 创建目录
        File dest = new File(filePath + newFileName);
        if (!dest.getParentFile().exists()) {
            dest.getParentFile().mkdirs();
        }

        try {
            // 保存文件
            file.transferTo(dest);
            
            // 构建返回结果
            SysFile sysFile = new SysFile();
            sysFile.setFileName(fileName);
            sysFile.setSysFileName(newFileName);
            
            // 获取访问URL
            String url = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + "/file/" + newFileName;
            sysFile.setUrl(url);
            sysFile.setUrlIp(url);
            
            return R.ok(sysFile);
        } catch (IOException e) {
            e.printStackTrace();
            return R.fail("文件上传失败：" + e.getMessage());
        }
    }

    /**
     * 头像上传
     */
    @PostMapping("/avatar")
    @Log(describe = "头像上传")
    public R<?> avatar(@RequestParam("file") MultipartFile file, HttpServletRequest request) {
        if (file.isEmpty()) {
            return R.fail("上传文件不能为空");
        }

        // 获取文件名
        String fileName = file.getOriginalFilename();
        // 获取文件后缀
        String suffixName = fileName.substring(fileName.lastIndexOf("."));
        // 生成新文件名
        String newFileName = "avatar_" + UUID.randomUUID() + suffixName;

        // 获取系统类型，选择存储路径
        String os = System.getProperty("os.name");
        String filePath;
        if (os.toLowerCase().startsWith("win")) {
            filePath = windowsPath;
        } else if (os.toLowerCase().startsWith("mac")) {
            filePath = macPath;
        } else {
            filePath = linuxPath;
        }

        // 创建目录
        File dest = new File(filePath + newFileName);
        if (!dest.getParentFile().exists()) {
            dest.getParentFile().mkdirs();
        }

        try {
            // 保存文件
            file.transferTo(dest);
            
            // 构建返回结果
            SysFile sysFile = new SysFile();
            sysFile.setFileName(fileName);
            sysFile.setSysFileName(newFileName);
            
            // 获取访问URL
            String url = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + "/file/" + newFileName;
            sysFile.setUrl(url);
            sysFile.setUrlIp(url);
            
            return R.ok(sysFile);
        } catch (IOException e) {
            e.printStackTrace();
            return R.fail("文件上传失败：" + e.getMessage());
        }
    }
}
