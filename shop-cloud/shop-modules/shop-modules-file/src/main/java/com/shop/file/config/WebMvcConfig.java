package com.shop.file.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration("fileWebMvcConfig")
public class WebMvcConfig implements WebMvcConfigurer {

    @Value("${file.windows.path}")
    private String windowsPath;

    @Value("${file.mac.path}")
    private String macPath;

    @Value("${file.linux.path}")
    private String linuxPath;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String os = System.getProperty("os.name");
        String filePath;
        if (os.toLowerCase().startsWith("win")) {
            filePath = "file:/" + windowsPath;
        } else if (os.toLowerCase().startsWith("mac")) {
            filePath = "file:" + macPath;
        } else {
            filePath = "file:" + linuxPath;
        }
        
        // 映射文件访问路径
        registry.addResourceHandler("/file/**")
                .addResourceLocations(filePath);
    }
}
