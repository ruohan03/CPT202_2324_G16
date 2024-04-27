package com.example.demo.Service;
import com.example.demo.Model.Reservation;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

    @Service
    public interface ReservationService {
        List<Reservation> getAllReservations();
    }

