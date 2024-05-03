package com.example.demo.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HXYZController {
    @GetMapping("/H-XYZHomePage")
    public String showHXYZ() {return "HXYZ";
    }

}
