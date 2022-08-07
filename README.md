# spring-security

  1. 스프링 시큐리티를 사용한 로그인

    > - java 11
    > - spring boot 2.6.3
    > - spring security 5.6.1

  2. 테스트용 swagger 추가
    - swagger 페이지 url > http://localhost:8081/swagger-ui/index.html

## 문제 해결

  1. X-Frame-Options to DENY 설정 때문에 iframe을 통해 접근할 수 없는 문제 해결
  
    - WebSecurityConfig
      > configure
      ```java
        http.headers().frameOptions().sameOrigin();  // 추가 (frame옵션 설정)
      ```

  2. Failed to start bean 'documentationPluginsBootstrapper'; nested exception is java.lang.NullPointerException 발생
  
    - Spring boot 2.6버전 이후에 spring.mvc.pathmatch.matching-strategy 값이 ant_apth_matcher에서 path_pattern_parser로 변경되면서 몇몇 라이브러리(swagger포함)에 오류가 발생
    ```yml
    spring:    
      mvc: #추가
        pathmatch:
          matching-strategy: ant_path_matcher 
    ```


## 메모

### 1. AuthenticationEntryPoint

인증과정에서 실패하거나 인증헤더(Authorization)를 보내지 않게되는 경우 401(UnAuthorized) 라는 응답

### 2. AccessDeniedHandler

권한이 충족되지 않으면 403(Forbidden) 접근금지 타입을 반환