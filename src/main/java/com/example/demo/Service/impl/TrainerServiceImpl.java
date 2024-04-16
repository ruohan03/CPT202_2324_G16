package com.example.demo.Service.impl;

import com.example.demo.Model.Trainer;
import com.example.demo.Service.TrainerService;
import com.example.demo.repo.TrainerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TrainerServiceImpl implements TrainerService {
    @Autowired
    private TrainerRepository TrainerRepository;

    @Override
    public void demoMethod() {
        System.out.println("This is a demo method");
    }

    @Override
    public void TrainerMethod() {

    }

    @Override
    public List<Trainer> findAllTrainer() {

        return this.TrainerRepository.findAll();
    }

    @Override
    public void addTrainer(Trainer trainer) {
        TrainerRepository.save(trainer);
    }
}
