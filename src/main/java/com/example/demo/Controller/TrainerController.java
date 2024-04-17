package com.example.demo.Controller;

import com.example.demo.Model.Trainer;
import com.example.demo.Service.TrainerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class TrainerController {
    @Autowired
    private TrainerService TrainerService;

    @GetMapping("/trainers")
    public String getAllTrainers(Model model){
        List<Trainer> trainers = TrainerService.findAllTrainer();
        System.out.println(trainers); // 用于调试，查看获取的训练师信息
        model.addAttribute("trainers", trainers);
        return "trainersView";
    }


    @PostMapping("/addTrainer")
    public String addTrainer(@ModelAttribute("trainer") Trainer trainer){
        TrainerService.addTrainer(trainer);
        return "redirect:/trainers"; // 重定向到显示所有训练师的页面
    }

    @GetMapping
    public ResponseEntity<List<Trainer>> getTrainersByCourse(@RequestParam String courseId) {
        List<Trainer> trainers = TrainerService.findTrainersByCourseId(courseId);
        return ResponseEntity.ok(trainers);
    }
}
