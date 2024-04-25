package com.example.demo.Service;

import com.example.demo.Model.Staff;

import java.util.List;
import java.util.Optional;

public interface StaffService {
    List<Staff> findAllStaff();

    void addStaff(Staff staff);

    List<Staff> findStaffByCourseId(String courseId);

    Optional<Staff> login(String phone, String password);

    Staff findStaffById(String staffId);

    boolean updateStaff(Staff staff);
}
