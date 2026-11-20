package com.example.farmstay;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.File;
@Configuration
public class WebConfig implements WebMvcConfigurer {
@Override
    public void addResourceHandlers(ResourceHandlerRegistry regis){
        File folder = new File("uploads").getAbsoluteFile();
        folder.mkdirs();
        regis.addResourceHandler("/uploads/**")
                .addResourceLocations(folder.toURI().toString());
    }
}
