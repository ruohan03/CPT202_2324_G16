
package com.example.demo.Model;

import jakarta.persistence.*;

@Entity
@Table(name = "course")
public class Course {
    @Id
    @Column(name = "course_id", nullable = false, length = 4)
    private String courseId;

    @Column(name = "name", nullable = true)
    private String name;

    @Column(name = "location", nullable = true)
    private String location;

    public Course() {}

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}