package com.benzhubenzhu.springboot01.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    //http://127.0.0.1:8888/hi
    @GetMapping("/hi")
    public String hello(){
        return "<h1 style='color:red'>我的第一个SpringBoot应用！hello world！<h1>";
    }

    @GetMapping("/")
    public String index(){
        return "这里是首页";
    }
}
