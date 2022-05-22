package com.jiashn.oneofficesso.config;

import com.jiashn.oneofficesso.user.domain.OpuAdmin;
import com.jiashn.oneofficesso.filter.JwtAuthenticationTokenFilter;
import com.jiashn.oneofficesso.handler.SelfDefinedAccessDeniedHandler;
import com.jiashn.oneofficesso.handler.SelfDefinedAuthenticationEntryPoint;
import com.jiashn.oneofficesso.user.service.OpuAdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.Objects;

/**
 * @author jiangjs
 * @date 2022-05-18 21:04
 */
@Configuration
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private OpuAdminService opuAdminService;
    @Autowired
    private SelfDefinedAccessDeniedHandler accessDeniedHandler;
    @Autowired
    private SelfDefinedAuthenticationEntryPoint authenticationEntryPoint;

    @Value("${security.release-url}")
    private String releaseUrl;


    /**
     * 使自定义的UserDetailsService生肖
     * @param auth 认证管理构造器
     * @throws Exception 异常
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService()).passwordEncoder(passwordEncoder());
    }

    /**
     * 不走 Spring Security 过滤器链,放行资源,登录接口等可以直接在此放行
     * @param web webSecurity
     * @throws Exception 异常
     */
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers(releaseUrl.split(","));
    }

    /**
     * 配置security
     * @param http HttpSecurity
     * @throws Exception 异常
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {

        //使用JWT,不需要CSRF
        http.csrf().disable()
                //基于token,不需要session
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.authorizeRequests()
                //允许登录访问
                //除了上面的,都需要认证
                .anyRequest()
                .authenticated()
                .and()
                //禁用缓存
                .headers()
                .cacheControl();
        //添加jwt登录授权过滤器
        http.addFilterBefore(jwtAuthenticationTokenFilter(), UsernamePasswordAuthenticationFilter.class);
        //添加自定义未授权和未登录返回
        http.exceptionHandling()
                .accessDeniedHandler(accessDeniedHandler)
                .authenticationEntryPoint(authenticationEntryPoint);
    }


    /**
     * 重写UserDetailService
     */
    @Override
    @Bean
    public UserDetailsService userDetailsService(){
        return username -> {
            OpuAdmin userInfo = opuAdminService.getUserInfoByUsername(username);
            return Objects.nonNull(userInfo) ? userInfo : null;
        };
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter(){
        return new JwtAuthenticationTokenFilter();
    }
}
