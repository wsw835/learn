package com.wensw.security.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: wensw
 * @date: 2021/10/2
 * @description:
 */
@RestController
@RequestMapping("test")
public class GenerateController {

    @GetMapping("/hello")
    public String getLogin() {
        return "hello Security";
    }
}
