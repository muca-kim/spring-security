package com.project.security.config;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithSecurityContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import lombok.extern.slf4j.Slf4j;

import static org.springframework.test.web.servlet.MockMvcBuilder.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Slf4j
@SpringBootTest
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
public class SecurityConfigTest {

    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private MockMvc mock;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    @WithMockUser(username = "admin", password = "admin", roles = { "admin" })
    public void setUp() {
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        Assert.assertNotNull(context);
        log.info("name : {}", authentication != null ? authentication.getName() : null);
        log.info("principal : {}", authentication != null ? authentication.getPrincipal() : null);
        log.info("authorities : {}", authentication != null ? authentication.getAuthorities() : null);
    }

    @Test
    public void test001() throws Exception {
        mock.perform(post("/login")
                .contentType("application/json")
                .param("userId", "admin").param("password", "admin"))
                .andExpect(status().is3xxRedirection())
                .andDo(print());
        // log.info("principal", authentication.getPrincipal());
        // log.info("details", authentication.getDetails());
        // Assert.assertNotNull(context);
        // Assert.assertEquals("admin", authentication.getName());
    }

    @Test
    public void test002() throws Exception {
        // String pw = passwordEncoder.encode("1111");
        mock.perform(post("/login")
                .contentType("application/json")
                .param("userId", "user5555").param("password", "1111"))
                .andExpect(status().is3xxRedirection()).andDo(print());
    }

}
