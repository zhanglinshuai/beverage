package com.cqz.beverage.config;

import com.cqz.beverage.interceptor.JwtTokenInterceptor;
import com.cqz.beverage.utils.JwtTokenUtil;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.List;

/**
 * Web配置
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private final JwtTokenUtil jwtTokenUtil;

    public WebMvcConfig(JwtTokenUtil jwtTokenUtil) {
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //配置拦截器
        registry.addInterceptor(new JwtTokenInterceptor(jwtTokenUtil))
                //拦截所有接口
                .addPathPatterns("/**")
                //排除无需登录的接口
                .excludePathPatterns(getExcludePaths());


    }

    /**
     * 无需登录的接口列表
     * @return
     */
    private List<String> getExcludePaths(){
        ArrayList<String> excludePaths = new ArrayList<>();
        excludePaths.add("/user/login");
        excludePaths.add("/user/register");
        return excludePaths;
    }
}
