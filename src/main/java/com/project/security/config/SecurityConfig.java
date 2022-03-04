package com.project.security.config;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutHandler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @EnableWebSecurity
    @Configuration
    @RequiredArgsConstructor
    public static class WebSecurityConfig extends WebSecurityConfigurerAdapter {

        private final PasswordEncoder passwordEncoder;

        @Override
        protected void configure(AuthenticationManagerBuilder auth) throws Exception {
            String encodedPassword = passwordEncoder.encode("admin");
            // 인메모리 유저 설정(관리자)
            auth.inMemoryAuthentication().withUser("admin").password(encodedPassword)
                    .roles("ADMIN");
            encodedPassword = passwordEncoder.encode("user001");
            auth.inMemoryAuthentication().withUser("user001").password(encodedPassword)
                    .roles("USER");

        }

        @Override
        public void configure(WebSecurity web) {
            web.ignoring().antMatchers("/resources/**");
        }

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.csrf().disable()
                    .authorizeRequests()
                    // 페이지 권한 설정
                    .antMatchers("/", "/login", "/logout", "/signup").permitAll()
                    .and()
                    // 로그인 설정
                    .formLogin().loginPage("/login").usernameParameter("userId").passwordParameter("password")
                    .permitAll()
                    .successHandler(new LoginSuccessHandler()).failureHandler(new LoginFailHandler())
                    .and()
                    // 로그아웃
                    .logout().addLogoutHandler(new CustomLogoutHandler());

            http.headers().frameOptions().sameOrigin();

        }

        @Configuration
        protected static class AuthenticationConfiguration extends GlobalAuthenticationConfigurerAdapter {
            @Autowired
            UserService userService;

            @Override
            public void init(AuthenticationManagerBuilder auth) throws Exception {
                // 인증할 유저 설정
                auth.userDetailsService(userService)
                        // 패스워드 인코딩
                        .passwordEncoder(new BCryptPasswordEncoder());
            }
        }

        protected class LoginSuccessHandler implements AuthenticationSuccessHandler {

            @Override
            public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                    Authentication authentication) throws IOException, ServletException {
                log.info("success={}", request.getRequestURI());
                setRedirectToPrevUrl(request, response);
            }
        }

        protected class LoginFailHandler implements AuthenticationFailureHandler {

            @Override
            public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                    AuthenticationException exception) throws IOException, ServletException {
                log.info("fail={}", request.getRequestURI());
                setRedirectToPrevUrl(request, response);
            }

        }

        protected class CustomLogoutHandler implements LogoutHandler {

            @Override
            public void logout(HttpServletRequest request, HttpServletResponse response,
                    Authentication authentication) {
                try {
                    setRedirectToPrevUrl(request, response);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }

        // 이전 url로 리다이렉트 설정
        protected void setRedirectToPrevUrl(HttpServletRequest request, HttpServletResponse response)
                throws IOException {
            String prevUrl = request.getRequestURI();
            log.info("prevUrl={}", prevUrl);
            if (prevUrl == null) {
                response.sendRedirect("/");
            } else {
                response.sendRedirect(prevUrl);
            }
        }

    }

}