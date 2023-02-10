package com.example.springsecurity.controllers;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {


    @GetMapping( "/hellouser")
    public  String helloUser() {
        return "Hello User";
    }


    @GetMapping("/helloadmin")
    public String helloAdmin() {
        return "Hello Admin";
    }

}
