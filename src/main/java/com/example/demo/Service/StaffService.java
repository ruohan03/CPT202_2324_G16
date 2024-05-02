package com.example.demo.Service;

import com.example.demo.Model.Member;
import com.example.demo.Model.Staff;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface StaffService {
    List<Staff> findAllStaff();

    void addStaff(Staff staff);

    List<Staff> findStaffByCourseId(String courseId);

    Optional<Staff> login(String phone, String password);

    Staff findStaffById(String staffId);

    boolean updateStaff(Staff staff);

    Staff findByPhoneAndName(String phone, String name);

    void sendRenewalNotifications();

    List<Member> findMembersWithSentNotifications();
    List<Member> findMembersForRenewalNotification(Date thresholdDate);
    void saveOrUpdate(Staff staff);

    List<Staff> findStaffByNameContaining(String name);

    List<Staff> findStaffByStaffIdContaining(String staffId);

    List<Staff> findStaffByCourseIdContaining(String courseId);

    boolean existsByStaffId(String staffId);

    void deleteByStaffId(String staffId);

    void editStaff(Staff updatedStaff);
}
