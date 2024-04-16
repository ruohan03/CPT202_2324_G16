package com.example.demo.Service.imp;

import com.example.demo.Model.Trainer;
import com.example.demo.Service.demoService;
import com.example.demo.repo.demoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class demoServiceImp implements demoService {
    @Autowired
    private demoRepository demoRepository;

    @Override
    public void demoMehtod() {
        System.out.println("This is a demo method");
    }

    @Override
    public List<Trainer> findallTrainer() {

        return this.demoRepository.findAll();
    }

    @Override
    public void addTrainer(Trainer trainer) {

    }
}
