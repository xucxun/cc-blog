package com.example.blog.config;

import com.example.blog.common.Constant;
import com.example.blog.util.BlogUtil;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    // 用来配置Filter链,对要拦截的目标资源进行配置
    @Override
    public void configure(WebSecurity ws) throws Exception {
        //忽略拦截 resources 下的所有静态资源
        ws.ignoring().antMatchers("/**/*.css", "/**/*.js", "/**/*.png", "/**/*.jpg", "/**/*.jpeg","/**/*.eot","/**/*" +
                ".ttf","/**/*.woff","/**/*.woff2");
    }

    //配置权限管理
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // 开启请求路径限制
        http.authorizeRequests()
                .antMatchers("/user/setting",
                        "/user/upload",
                        "/article/save",
                        "/article/file/upload",
                        "/comment/save/**",
                        "/message/**",
                        "/notice/**",
                        "/like",
                        "/follow",
                        "/unfollow"
                        )
                .hasAnyAuthority(
                        Constant.AUTHORITY_USER,
                        Constant.AUTHORITY_ADMIN,
                        Constant.AUTHORITY_SUPER_ADMIN
                        )
                .antMatchers(
                        "/article/delete",
                        "/article/top",
                        "/article/untop",
                        "/article/morrow",
                        "/article/morrow"
                        )
                .hasAnyAuthority(
                        Constant.AUTHORITY_ADMIN,
                        Constant.AUTHORITY_SUPER_ADMIN
                        )
                .antMatchers(
                        "/admin/**",
                        "/actuator/**"
                )
                .hasAnyAuthority(
                        Constant.AUTHORITY_SUPER_ADMIN
                )
                .anyRequest().permitAll()
                .and().csrf().disable();
                 //不启用csrf的检查;

        // 如果权限不够时的处理
        http.exceptionHandling()
                // 没有登录时的处理
                .authenticationEntryPoint(new AuthenticationEntryPoint() {
                    @Override
                    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
                        // 如果请求x-requested-with 中头包含XMLHttpRequest 说明是异步请求
                        String xRequestedWith = httpServletRequest.getHeader("x-requested-with");
                        if ("XMLHttpRequest".equals(xRequestedWith)) {
                            // 设置响应体为 json 格式
                            httpServletResponse.setContentType("application/plain;charset=utf-8");
                            // 拿到输出流,输出返回内容给前端页面
                            PrintWriter writer = httpServletResponse.getWriter();
                            writer.write(BlogUtil.getJsonResult(403, "你还没有登录哦!"));
                        }else {
                            //请求重定向到登录页面
                           httpServletResponse.sendRedirect(httpServletRequest.getContextPath() + "/login");
                        }
                    }
                })
                .accessDeniedHandler(new AccessDeniedHandler() {
                    @Override
                    public void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AccessDeniedException e) throws IOException, ServletException {
                        String xRequestedWith = httpServletRequest.getHeader("x-requested-with");
                        if ("XMLHttpRequest".equals(xRequestedWith)) {
                            httpServletResponse.setContentType("application/plain;charset=utf-8");
                            PrintWriter writer = httpServletResponse.getWriter();
                            writer.write(BlogUtil.getJsonResult(403, "你没有访问此功能的权限!"));
                        }else {
                            httpServletResponse.sendRedirect(httpServletRequest.getContextPath() + "/denied");
                        }
                    }
                });

        // Security底层默认会拦截/logout请求,进行退出处理.
        // 覆盖它默认的逻辑,才能执行我们自己的退出代码.
        http.logout().logoutUrl("/securitylogout");
    }
}
