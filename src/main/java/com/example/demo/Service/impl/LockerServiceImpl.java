package com.example.demo.Service.impl;

import com.example.demo.Model.Locker;
import com.example.demo.Service.LockerService;
import com.example.demo.repo.LockerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class LockerServiceImpl implements LockerService {
    @Autowired
    private LockerRepository lockerRepository;

    @Override
    public Locker findLockerById(String lockerId) {
        return lockerRepository.findById(lockerId).orElse(null);
    }

    @Override
    public Locker reserveLocker(String lockerId) {
        Locker locker = findLockerById(lockerId);
        if (locker != null && !locker.isReserved()) {
            locker.setReserved(true);
            lockerRepository.save(locker);
        }
        return locker;
    }

    @Override
    public void returnLocker(String lockerId) {
        Locker locker = findLockerById(lockerId);
        if (locker != null && locker.isReserved()) {
            locker.setReserved(false);
            lockerRepository.save(locker);
        }
    }

    @Override
    public List<Locker> getAllLockers() {
        return (List<Locker>) lockerRepository.findAll();  // 假设 LockerRepository 有 findAll() 方法
    }
}
