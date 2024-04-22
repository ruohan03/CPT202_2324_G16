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
    Optional<Staff> findById(String StaffId);
    Staff save(Staff staff);
    void deleteById(String id);

    Optional<Staff> login(String phone, String password);


    Staff findStaffById(String staffId);


    boolean updateStaff(Staff staff);
}
