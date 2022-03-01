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
        protected void configure(AuthenticationManagerBuilder auth) throws Exception {
            // 인메모리 유저 설정(관리자)
            auth.inMemoryAuthentication().withUser("admin").password("admin").roles("ADMIN");

        }

        @Override
        public void configure(WebSecurity web) {
            web.ignoring().antMatchers("/resources/**");
        }

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.authorizeRequests()
                    // 페이지 권한 설정
                    .antMatchers("/", "login", "logout").permitAll()
                    .antMatchers("/page").authenticated()
                    .and()
                    // 로그인 설정
                    .formLogin().loginPage("/login").usernameParameter("userId").passwordParameter("userPassword")
                    .successHandler(new LoginSuccessHandler()).failureHandler(new LoginFailHandler()).permitAll()
                    .and()
                    // 로그아웃
                    .logout().deleteCookies("isLogin").invalidateHttpSession(false).logoutUrl("/logout")
                    .addLogoutHandler(new CustomLogoutHandler());

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
                setRedirectToSameUrl(request, response);
            }
        }

        protected class LoginFailHandler implements AuthenticationFailureHandler {

            @Override
            public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                    AuthenticationException exception) throws IOException, ServletException {
                setRedirectToSameUrl(request, response);
            }

        }

        protected class CustomLogoutHandler implements LogoutHandler {

            @Override
            public void logout(HttpServletRequest request, HttpServletResponse response,
                    Authentication authentication) {
                try {
                    setRedirectToSameUrl(request, response);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }

        private void setRedirectToSameUrl(HttpServletRequest request, HttpServletResponse response) throws IOException {
            String prevPage = request.getHeader("Referer");
            if (prevPage == null) {
                response.sendRedirect("/");
            } else {
                response.sendRedirect(prevPage);
            }
        }

    }

}