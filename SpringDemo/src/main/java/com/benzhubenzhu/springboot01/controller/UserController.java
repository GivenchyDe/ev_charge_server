package com.benzhubenzhu.springboot01.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    // 登录：http://127.0.0.1:8888/user/login?username=admin&password=123456
    @GetMapping("/user/login")
    public String login(@RequestParam(required = false) String username,
                       @RequestParam(required = false) String password){
        if(username != null && password != null &&
           username.equals("admin") && password.equals("123456")) {
            return "登录成功！";
        }
        return "登录失败！";
    }
    // 注册：http://127.0.0.1:8888/user/register?username=admin&password=123456
    @GetMapping("/user/register")
    public String register(@RequestParam(required = false) String username,
                          @RequestParam(required = false) String password){
        if(username != null && password != null &&
           username.equals("admin") && password.equals("123456")) {
            return "注册成功！";
        }
        return "注册失败！";
    }
}
