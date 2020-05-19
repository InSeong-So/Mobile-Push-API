package com.sis.api.push.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.sis.api.push.security.ApiKeyInterceptor;

@Configuration
public class InterceptorConfig implements WebMvcConfigurer
{
    
    @Bean
    public ApiKeyInterceptor apiKeyInterceptor()
    {
        return new ApiKeyInterceptor();
    }
    
    @Override
    public void addInterceptors(InterceptorRegistry registry)
    {
        registry.addInterceptor(apiKeyInterceptor());
    }
}
