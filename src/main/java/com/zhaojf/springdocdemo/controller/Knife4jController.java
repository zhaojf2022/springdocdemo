package com.zhaojf.springdocdemo.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "测试 Knife4j")
@RestController
@RequestMapping("/knife4j")
public class Knife4jController {

    @Operation(summary ="测试")
    @GetMapping(value ="/test")
    public String test() {
        return "沉默王二又帅又丑";
    }
}