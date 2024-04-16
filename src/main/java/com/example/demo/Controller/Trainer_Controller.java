package com.example.demo.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Trainer_Controller {
    @GetMapping("/api/hello")
    public String SayHello(){
        return "Hello";
    }
}
