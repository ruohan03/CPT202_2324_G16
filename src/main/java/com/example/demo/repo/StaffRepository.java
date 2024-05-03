package com.example.demo.repo;

import com.example.demo.Model.Course;
import com.example.demo.Model.Staff;  // Make sure to import the Staff model
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository("fc_appointment_system/staff")
public interface StaffRepository extends JpaRepository<Staff, String> {
    List<Staff> findByNameContaining(String name);

    List<Staff> findStaffByCourseId(String courseId);

    Staff findByPhone(String phone);

    List<Staff> findStaffByCourseIdAndLevelLessThanEqual(String courseId, int level);

    Staff findByPhoneAndName(String phone, String name);

    List<Staff> findByStaffIdContaining(String staffId);

    List<Staff> findByGender(String gender);

    List<Staff> findByLevel(int level);

    List<Staff> findByCourseIdContaining(String courseId);

}
