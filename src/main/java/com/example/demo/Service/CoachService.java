package com.example.demo.Service;

import com.example.demo.Model.Staff;

import java.util.List;

public interface CoachService {
    List<Staff> findCoachesByCourse(String courseId);
    List<Staff> findCoachesByCourseAndLevel(String courseId, int maxLevel);
}
