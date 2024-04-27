package com.example.demo.repo;
import com.example.demo.Model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

    @Repository
    public interface ReservationRepository extends JpaRepository<Reservation,String> {

    }

