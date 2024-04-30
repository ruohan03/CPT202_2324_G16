
package com.example.demo.Service;

import com.example.demo.Model.Course;
import com.example.demo.repo.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

public interface CourseService {
    List<Course> findAllCourses();

    Course findCourseById(String id);
    void editCourse(Course updatedCourse);

    String findCourseNameById(String courseId);

    List<Course> findCoursesByNameContaining(String name);

    List<Course> findCoursesByCourseIdContaining(String courseId);

    void updateCourse(String courseId, Course updatedCourse);

    void addCourse(Course course);
    boolean existsByCourseId(String courseId);

    void deleteCourse(String courseId);
    boolean hasForeignKey(String courseId);
}

