package com.example.demo.Service.impl;

import com.example.demo.Model.Staff;
import com.example.demo.Service.TrainerService;
import com.example.demo.repo.StaffRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TrainerServiceImpl implements TrainerService {


    @Autowired
    private StaffRepository staffRepository;  // 使用您已经定义的 StaffRepository

    @Override
    public List<Staff> findCoachesByCourse(String courseId) {
        // 调用您在仓库中定义的方法
        return staffRepository.findStaffByCourseId(courseId);
    }

    @Override
    public List<Staff> findCoachesByCourseAndLevel(String courseId, int maxLevel) {
        return staffRepository.findStaffByCourseIdAndLevelLessThanEqualAndIsActive(courseId, maxLevel, true);
    }

}
