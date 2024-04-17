package com.example.demo.Service;

import com.example.demo.Model.Locker;
import java.util.List;

public interface LockerService {
    Locker findLockerById(String lockerId);
    Locker reserveLocker(String lockerId);
    void returnLocker(String lockerId);
    List<Locker> getAllLockers();  // 新增方法定义
}
