package com.example.demo.Controller;

import com.example.demo.Model.Trainer;
import com.example.demo.Service.demoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.MatrixVariable;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class demoController {
    @Autowired
    private demoService demoService;

    @GetMapping("/")
    public String getallTrainers(Model model){
        List<Trainer> trainerList=demoService.findallTrainer();
        model.addAttribute("trainers",trainerList);
        return "index";
    }
    @PostMapping("/addTrainer")
    public String addTrainer(@ModelAttribute("trainer") Trainer trainer){
        demoService.addTrainer(trainer);
        return "redirect:/";
    }
}
