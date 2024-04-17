package com.example.demo.Service.impl;

import com.example.demo.Model.Trainer;
import com.example.demo.Service.TrainerService;
import com.example.demo.repo.TrainerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.demo.exception.ResourceNotFoundException;


import java.util.List;

@Service
public class TrainerServiceImpl implements TrainerService {
    @Autowired
    private TrainerRepository trainerRepository;

    @Override
    public void demoMethod() {
        System.out.println("This is a demo method");
    }

    @Override
    public void TrainerMethod() {

    }

    @Override
    public List<Trainer> findAllTrainer() {

        return this.trainerRepository.findAll();
    }

    @Override
    public void addTrainer(Trainer trainer) {
        trainerRepository.save(trainer);
    }

    @Override
    public List<Trainer> findTrainersByCourseId(String courseId) {
        List<Trainer> trainers = trainerRepository.findByCourseId(courseId);
        if (trainers.isEmpty()) {
            // Handle the case where no trainers are found
            throw new ResourceNotFoundException("No trainers found for Course ID: " + courseId);
        }
        return trainers;
    }

    @Override
    public List<Trainer> findTrainersByCourseId(Long courseId) {
        return List.of();
    }
}
