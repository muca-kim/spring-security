package com.project.security.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class MainController {

    @GetMapping(name = "/")
    public String viewMain() {
        log.info("start main");
        return "main";
    }
}