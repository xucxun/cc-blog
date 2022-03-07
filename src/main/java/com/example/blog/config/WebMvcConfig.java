package com.example.blog.config;

import com.example.blog.controller.interceptor.LoginTicketInterceptor;
import com.example.blog.controller.interceptor.MessageInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Autowired
    private LoginTicketInterceptor loginTicketInterceptor;

    @Autowired
    private MessageInterceptor messageInterceptor;

    @Value("${blog.path.upload}")
    private String uploadPath;


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginTicketInterceptor)
                .excludePathPatterns("/**/*.css", "/**/*.js", "/**/*.png", "/**/*.jpg", "/**/*.jpeg");


        registry.addInterceptor(messageInterceptor)
                .excludePathPatterns("/**/*.css", "/**/*.js", "/**/*.png", "/**/*.jpg", "/**/*.jpeg");


    }

    // 文件保存在真实物理路径/upload/下（即项目的物理地址下：g:/work/data/upload）
    // 访问的时候使用虚虚拟路径/upload，比如文件名为1.png，就直接/upload/1.png就ok了。
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        //将物理地址upload下的文件映射到/upload下
        //访问的时候就直接访问http://localhost:8080/upload/文件名
        registry.addResourceHandler("/upload/**")
                .addResourceLocations("file:"+uploadPath+"/");
    }

}
