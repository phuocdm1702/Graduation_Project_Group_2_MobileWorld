package com.example.graduation_project_group_2_mobileworld.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api")
public class Test {

    @GetMapping("/hello")
    public String sayHello() {
        return "Bạn Huan mua da`u bưởi!";
    }

}
