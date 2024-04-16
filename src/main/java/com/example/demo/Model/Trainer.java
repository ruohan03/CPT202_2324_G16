package com.example.demo.Model;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Trainer {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private long id;
    @Id
    private String trainerId;
    private String name;
    private int phone;
    private String gender;
    private int level;
    private int salary;
    private String courseId;

    // Constructors
    public Trainer() {
    }

    public Trainer(String trainerId, String name, int phone, String gender, int level, int salary, String courseId) {
        this.trainerId = trainerId;
        this.name = name;
        this.phone = phone;
        this.gender = gender;
        this.level = level;
        this.salary = salary;
        this.courseId = courseId;
    }

    // Getters and setters
    public String getTrainerId() {
        return trainerId;
    }

    public void setTrainerId(String trainerId) {
        this.trainerId = trainerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPhone() {
        return phone;
    }

    public void setPhone(int phone) {
        this.phone = phone;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getSalary() {
        return salary;
    }

    public void setSalary(int salary) {
        this.salary = salary;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }
}
