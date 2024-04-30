
package com.example.demo.repo;

import com.example.demo.Model.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;


@Repository
public interface CourseRepository extends JpaRepository<Course, String> {
    List<Course> findByNameContaining(String name);
    List<Course> findByCourseIdContaining(String courseId);
}