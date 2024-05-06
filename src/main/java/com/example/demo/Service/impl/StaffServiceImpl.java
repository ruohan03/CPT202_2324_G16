package com.example.demo.Service.impl;
import com.example.demo.Model.Course;
import com.example.demo.Model.Member;
import com.example.demo.Model.Staff;  // Ensure you import the Staff model
import com.example.demo.Service.StaffService;
import com.example.demo.Service.PasswordService;
import com.example.demo.repo.CourseRepository;
import com.example.demo.repo.MemberRepository;
import com.example.demo.repo.StaffRepository;  // Import the StaffRepository
import com.example.demo.repo.ReservationRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.demo.exception.ResourceNotFoundException;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class StaffServiceImpl implements StaffService {

    @Autowired
    private StaffRepository staffRepository;

    @Autowired
    private PasswordService passwordService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private ReservationRepository reservationRepository;

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
    public Optional<Staff> login(String phone, String password) {
        Staff staff = staffRepository.findByPhone(phone);
        if (staff != null && passwordService.matchPassword(password, staff.getEncodedPassword())) {
            if (staff.isActive()) {
                return Optional.of(staff);
            }
        }
        return Optional.empty();
    }


    @Override
    public Staff findStaffById(String staffId) {
        return staffRepository.findById(staffId).orElse(null);
    }

    @Override
    public Staff findByPhoneAndName(String phone, String name) {
        return staffRepository.findByPhoneAndName(phone, name);
    }

    @Transactional
    @Override
    public boolean updateStaff(Staff staff) {
        return staffRepository.findById(staff.getStaffId())
                .map(existingStaff -> {
                    // 更新员工信息，包括 isActive 状态
                    existingStaff.setName(staff.getName());
                    existingStaff.setPhone(staff.getPhone());
                    existingStaff.setGender(staff.getGender());
                    existingStaff.setLevel(staff.getLevel());
                    existingStaff.setSalary(staff.getSalary());
                    existingStaff.setCourseId(staff.getCourseId());
                    existingStaff.setActive(staff.isActive());  // 更新 isActive 状态

                    staffRepository.save(existingStaff);
                    return true;
                })
                .orElse(false);  // 如果找不到指定的员工，返回false
    }

    @Override
    public void sendRenewalNotifications() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, 15);
        Date thresholdDate = cal.getTime();
        List<Member> members = memberRepository.findMembersForRenewalNotification(thresholdDate);
        for (Member member : members) {
            member.setRenewalNotificationSent(true);
            memberRepository.save(member);
        }
    }

    @Override
    public List<Member> findMembersWithSentNotifications() {
        return memberRepository.findByRenewalNotificationSent(true);
    }

    @Override
    public List<Member> findMembersForRenewalNotification(Date thresholdDate) {
        return memberRepository.findMembersExpiringBefore(thresholdDate);
    }
    @Override
    public void saveOrUpdate(Staff staff) {
        staffRepository.save(staff);
    }

    @Override
    public List<Staff> findStaffByNameContaining(String name) {
        return staffRepository.findByNameContaining(name);
    }
    @Override
    public List<Staff> findStaffByStaffIdContaining(String staffId) {
        return staffRepository.findByStaffIdContaining(staffId);
    }

    @Override
    public List<Staff> findStaffByCourseIdContaining(String courseId) {
        return staffRepository.findByCourseIdContaining(courseId);
    }

    @Override
    public boolean existsByStaffId(String staffId) {
        return staffRepository.existsById(staffId);
    }

    @Override
    public String findNameByStaffId(String staffId) {
        Staff staff = staffRepository.findById(staffId).orElse(null);
        return staff != null ? staff.getName() : null;
    }
}
