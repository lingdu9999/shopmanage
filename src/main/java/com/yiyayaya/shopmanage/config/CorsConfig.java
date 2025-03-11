package com.yiyayaya.shopmanage.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {
  private static final long MAX_AGE = 24 * 60 * 60;


  @Override
  public void addCorsMappings(CorsRegistry registry) {
    registry.addMapping("/upload")
            .allowedOrigins("http://localhost:3000","http://localhost:5173","http://localhost:5173")
            .allowedMethods("POST")
            .allowedHeaders("*")
            .maxAge(MAX_AGE);
  }

}
