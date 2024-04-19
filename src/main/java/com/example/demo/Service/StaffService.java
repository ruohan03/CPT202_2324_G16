package com.example.demo.Service;

import com.example.demo.Model.Staff;

import java.util.List;
import java.util.Optional;

public interface StaffService {
    void demoMethod();

    void staffMethod();

    List<Staff> findAllStaff();

    void addStaff(Staff staff);

    List<Staff> findStaffByCourseId(String courseId);

    List<Staff> findAll();
    Optional<Staff> findById(String id);
    Staff save(Staff staff);
    void deleteById(String id);
}
