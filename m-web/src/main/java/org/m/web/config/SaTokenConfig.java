package org.m.web.config;

import cn.dev33.satoken.context.SaHolder;
import cn.dev33.satoken.interceptor.SaInterceptor;
import cn.dev33.satoken.jwt.StpLogicJwtForMixin;
import cn.dev33.satoken.stp.StpLogic;
import cn.dev33.satoken.stp.StpUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;

@Configuration
public class SaTokenConfig implements WebMvcConfigurer {
    // 注册拦截器
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new SaInterceptor(handle -> {
            try {
//                System.out.println("-------- 前端访问path：" + SaHolder.getRequest().getRequestPath());
                StpUtil.checkLogin();
//                System.out.println("-------- 此 path 校验成功：" + SaHolder.getRequest().getRequestPath());
            } catch (Exception e) {
//                System.out.println("-------- 此 path 校验失败：" + SaHolder.getRequest().getRequestPath());
                throw e;
            }
        })).addPathPatterns("/**").excludePathPatterns(new String[]{"/user/doLogin", "/sse/**", "/favicon.ico", "/", "/login",
                "/*.html", "/**/*.css", "/**/*.js", "/v3/api-docs/**", "/assets/**", "/error", "/payConfig/pay", "/payConfig/getPay/", "/payOrder/page", "/sse/**"});
        ;
    }

    //....省略其他代码

    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE"));// 支持请求方式
        config.addAllowedOriginPattern("*");// 支持跨域
        config.setAllowCredentials(true);// cookie
        config.addAllowedHeader("*");// 允许请求头信息
        config.addExposedHeader("*");// 暴露的头部信息

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);// 添加地址映射
        return new CorsFilter(source);
    }

    @Bean
    public StpLogic getStpLogicJwt() {
        return new StpLogicJwtForMixin();
    }
}
