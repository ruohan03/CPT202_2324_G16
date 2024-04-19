
package com.example.demo.repo;

import com.example.demo.Model.Course;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import java.util.List;


@Repository
public interface CourseRepository extends CrudRepository<Course, String> {
    List<Course> findByNameContaining(String name);
}






