package com.example.demo.Controller;

import com.example.demo.Model.Trainer;
import com.example.demo.Service.TrainerService;
import com.example.demo.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class TrainerController {
    @Autowired
    private TrainerService trainerService;

    @GetMapping("/trainers")
    public String getAllTrainers(Model model){
        List<Trainer> trainers = trainerService.findAllTrainer();
        System.out.println(trainers); // 用于调试，查看获取的训练师信息
        model.addAttribute("trainers", trainers);
        return "trainersView";
    }

    @PostMapping("/addTrainer")
    public String addTrainer(@ModelAttribute("trainer") Trainer trainer){
        trainerService.addTrainer(trainer);
        return "redirect:/trainers"; // 重定向到显示所有训练师的页面
    }

    @GetMapping("/trainers/course/{courseId}")
    public ResponseEntity<List<Trainer>> getTrainersByCourseId(@PathVariable String courseId) {
        try {
            List<Trainer> trainers = trainerService.findTrainersByCourseId(courseId);
            return ResponseEntity.ok().body(trainers);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/courses/{courseId}/trainers")
    public String getCourseTrainers(@PathVariable String courseId, Model model) {
        List<Trainer> trainers = trainerService.findTrainersByCourseId(courseId);
        model.addAttribute("trainers", trainers);
        model.addAttribute("courseName", courseId); // 假设您已经有方法获取课程名称
        return "course-trainers"; // Thymeleaf 模板名称
    }


}
