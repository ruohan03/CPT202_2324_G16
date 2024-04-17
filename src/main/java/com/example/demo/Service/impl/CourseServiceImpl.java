package com.example.demo.Service.impl;

import com.example.demo.Model.Course;
import com.example.demo.Service.CourseService;
import com.example.demo.repo.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseServiceImpl implements CourseService {

    @Autowired
    private CourseRepository courseRepository;

    @Override
    public List<Course> findAllCourses() {
        return (List<Course>) courseRepository.findAll();
    }

    @Override
    public List<String> getAvailableCourseTypes() {
        return List.of();
    }
}
