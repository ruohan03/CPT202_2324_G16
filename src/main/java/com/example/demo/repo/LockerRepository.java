package com.example.demo.repo;

import com.example.demo.Model.Locker;
import org.springframework.data.repository.CrudRepository;

public interface LockerRepository extends CrudRepository<Locker, String> {
}
