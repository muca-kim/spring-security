package com.project.security.controller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.MockMvcBuilder.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.HashMap;
import java.util.Map;

import javax.transaction.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private MockMvc mock;

    @Test
    @Transactional
    public void test001() throws Exception {
        mock.perform(post("/signup")
                .contentType("application/json")
                .content(content()))
                .andExpect(status().isOk()).andDo(print());
    };

    private String content() throws JsonProcessingException {
        Map<String, String> con = new HashMap<>();
        con.put("userId", "user5555");
        con.put("password", "1111");
        con.put("address", "Seoul");
        con.put("name", "kim");
        con.put("phoneNo", "111-1111");
        con.put("role", "USER");
        return mapper.writeValueAsString(con);
    }
    // {
    // "userId": "user1111",
    // "password": "1111",
    // "name": "홍길동",
    // "address": "Seoul",
    // "role": "USER",
    // "phoneNo": "123-4567"
    // }
}
