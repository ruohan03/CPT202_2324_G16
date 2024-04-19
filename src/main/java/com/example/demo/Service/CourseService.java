
package com.example.demo.Service;

import com.example.demo.Model.Course;
import com.example.demo.repo.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

public interface CourseService {
    List<Course> findAllCourses();

    List<String> getAvailableCourseTypes();

    Course findCourseById(String id);

    String findCourseNameById(String courseId);

    List<Course> findCoursesByNameContaining(String name);
}

