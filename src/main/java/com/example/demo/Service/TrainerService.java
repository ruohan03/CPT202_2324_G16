package com.example.demo.Service;

import com.example.demo.Model.Trainer;

import java.util.List;

public interface TrainerService {
    void demoMethod();

    void TrainerMethod();

    List<Trainer> findAllTrainer();

    void addTrainer(Trainer trainer);

    List<Trainer> findTrainersByCourseId(String courseId);

    List<Trainer> findTrainersByCourseId(Long courseId);
}
