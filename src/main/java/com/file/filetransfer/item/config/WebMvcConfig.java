package com.file.filetransfer.item.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer{

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        //静态资源映射地址
        registry.addResourceHandler("/upload/**")  //映射地址
                .addResourceLocations("file:E:/upload/") //本机地址
                .addResourceLocations("file:/opt/filetransfer/upload/");//映射到linux地址

        //静态资源映射地址
        registry.addResourceHandler("/file/**")   //url地址
                .addResourceLocations("file:E:/upload/") //本机地址
                .addResourceLocations("file:/opt/filetransfer/file/");//映射到linux地址
    }
}
