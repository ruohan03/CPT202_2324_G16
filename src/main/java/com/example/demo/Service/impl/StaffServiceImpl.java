package com.example.demo.Service.impl;
import com.example.demo.Model.Staff;  // Ensure you import the Staff model
import com.example.demo.Service.StaffService;
import com.example.demo.repo.StaffRepository;  // Import the StaffRepository
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.demo.exception.ResourceNotFoundException;

import java.util.List;
import java.util.Optional;

@Service
public class StaffServiceImpl implements StaffService {
    @Autowired
    private StaffRepository staffRepository;

    @Override
    public void demoMethod() {
        System.out.println("This is a demo method");
    }

    @Override
    public void staffMethod() {
        // Implement specific methods for staff
    }

    @Override
    public List<Staff> findAllStaff() {
        return this.staffRepository.findAll();
    }

    @Override
    public void addStaff(Staff staff) {
        staffRepository.save(staff);
    }

    @Override
    public List<Staff> findStaffByCourseId(String courseId) {
        List<Staff> staffs = staffRepository.findStaffByCourseId(courseId);
        if (staffs.isEmpty()) {
            // Handle the case where no staff are found
            throw new ResourceNotFoundException("No staff found for Course ID: " + courseId);
        }
        return staffs;
    }

    @Override
    public List<Staff> findAll() {
        return null;
    }

    @Override
    public Optional<Staff> findById(String id) {
        return Optional.empty();
    }

    @Override
    public Staff save(Staff staff) {
        return null;
    }

    @Override
    public void deleteById(String id) {

    }

    @Override
    public Optional<Staff> login(String phone, String password) {
        Optional<Staff> staff = staffRepository.findByPhone(phone);
        if(staff.isPresent() && staff.get().getPassword().equals(password)) {
            return staff;
        }
        return Optional.empty();
    }
}
