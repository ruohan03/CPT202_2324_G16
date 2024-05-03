package com.example.demo.Service.impl;

import com.example.demo.Model.Course;
import com.example.demo.Service.CourseService;
import com.example.demo.repo.CourseRepository;
import jakarta.persistence.EntityManager;
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
    public boolean checkCourseActive(Course course) {
        return course.isActive(); // Directly return the isActive status from the Course object
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

    @Override
    public void updateCourse(String courseId, Course updatedCourse) {
        Course existingCourse = courseRepository.findById(courseId).orElse(null);
        if (existingCourse != null) {
            existingCourse.setName(updatedCourse.getName());
            existingCourse.setLocation(updatedCourse.getLocation());
            existingCourse.setActive(updatedCourse.isActive());  // 更新 isActive 状态
            courseRepository.save(existingCourse);
        }
    }

    @Override
        public void addCourse(Course course) {
            courseRepository.save(course);
        }
    @Override
    public boolean existsByCourseId(String courseId) {
        // Check if a course with the given courseId exists in the database
        return courseRepository.existsById(courseId);
    }
    @Override
    public List<Course> getActiveCourses() {
        return courseRepository.findByIsActiveTrue();
    }
    @Override
    public boolean existsByCourseIdAndIsActive(String courseId, boolean isActive) {
        return courseRepository.existsByCourseIdAndIsActive(courseId, isActive);
    }
}



