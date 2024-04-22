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

    public Course findCourseById(String id) {
        return courseRepository.findById(id).orElse(null);
    }
    @Override
    public String findCourseNameById(String courseId) {
        Course course = findCourseById(courseId);
        return course != null ? course.getName() : "Course not found";
    }

    @Override
    public List<Course> findCoursesByNameContaining(String name) {
        return courseRepository.findByNameContaining(name);
    }

    @Override
    public List<Course> findCoursesByCourseIdContaining(String courseId) {
        return courseRepository.findByCourseIdContaining(courseId);
    }


}
