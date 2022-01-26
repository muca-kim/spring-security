package com.project.security.config;

import com.project.security.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.GlobalAuthenticationConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @EnableWebSecurity
    @Configuration
    public static class WebSecurityConfig extends WebSecurityConfigurerAdapter {

        @Override
        public void configure(WebSecurity web) {
            web.ignoring().antMatchers("/css/**", "/script/**", "/images/**");
        }

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            // http.authorizeRequests()
            // // 페이지 권한 설정
            // .antMatchers("/**").permitAll().and()
            // // 로그인 설정
            // .formLogin().loginPage("/login").usernameParameter("userId").passwordParameter("userPassword")
            // .successHandler(new LoginSuccessHandler()).failureHandler(new
            // LoginFailHandler()).permitAll().and()
            // // 로그아웃
            // .logout().deleteCookies("isLogin").invalidateHttpSession(false).logoutUrl("/logout")
            // .addLogoutHandler(new CustomLogoutHandler()).and()
            // // 403 예외처리 핸들링
            // .exceptionHandling().accessDeniedPage("/");

        }

        @Configuration
        protected static class AuthenticationConfiguration extends GlobalAuthenticationConfigurerAdapter {

            // @Autowired
            // UserService userService;

            // @Override
            // public void init(AuthenticationManagerBuilder auth) throws Exception {
            // // 인증할 유저 설정
            // auth.userDetailsService(userService)
            // // 패스워드 인코딩
            // .passwordEncoder(new BCryptPasswordEncoder());
            // }
        }

    }
}