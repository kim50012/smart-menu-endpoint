package com.basoft.api.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.basoft.api.security.RestAuthenticationEntryPoint;
import com.basoft.api.security.TokenAccessDeniedHandler;
import com.basoft.api.security.TokenAuthenticationTokenFilter;

/**
 * Created by Administrator on 2017-11-21.
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    public void configureAuthentication(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder
                // 设置UserDetailsService
                .userDetailsService(this.userDetailsService)
                // 使用BCrypt进行密码的hash
                .passwordEncoder(passwordEncoder());
    }
    
    /**
     * 装载BCrypt密码编码器
     * 
     * @return
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                // 由于使用的是JWT，我们这里不需要csrf
                .csrf().disable()

                // 基于token，所以不需要session
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()

                .authorizeRequests()

                // 允许对于网站静态资源的无授权访问
                .antMatchers(
                        HttpMethod.GET,"/.well-known/pki-validation/fileauth.txt"
                ).permitAll()
                // 对于获取token的RESTAPI要允许匿名访问
                .antMatchers("/admin/loginCheck").permitAll()
                .antMatchers("/findCodeLikeBykey").permitAll()
                .antMatchers("/findCodeBykey").permitAll()
                .antMatchers("/images/**").permitAll() // 获取图片
                .antMatchers("/res/**").permitAll() // 获取静态资源
                //. antMatchers("/admin/fus").permitAll() // 暂时放开图片本地上传
                .antMatchers("/weixin/process").permitAll()
                .antMatchers("/ueditor/exec").permitAll()
                
                // 除上面外的所有请求全部需要鉴权认证
                .anyRequest().authenticated()
                .and().exceptionHandling().accessDeniedHandler(new TokenAccessDeniedHandler())
                .and().exceptionHandling().authenticationEntryPoint(new RestAuthenticationEntryPoint());
        // 禁用缓存
        httpSecurity.headers().cacheControl();

        // 添加JWT filter
        httpSecurity.addFilterBefore(authenticationTokenFilterBean(), UsernamePasswordAuthenticationFilter.class);
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers(HttpMethod.OPTIONS, "/**");
    }
    
    @Bean
    public TokenAuthenticationTokenFilter authenticationTokenFilterBean() throws Exception {
        return new TokenAuthenticationTokenFilter();
    }
}