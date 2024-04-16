package com.example.demo.Service;

import com.example.demo.Model.Trainer;

import java.util.List;

public interface demoService {
    void demoMehtod();
    List<Trainer> findallTrainer();

    void addTrainer(Trainer trainer);
}
