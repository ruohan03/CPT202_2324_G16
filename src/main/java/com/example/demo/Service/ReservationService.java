package com.example.demo.Service;
import com.example.demo.Model.Reservation;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

    @Service
    public interface ReservationService {

        List<Reservation> getAllReservations();

        boolean isCoachAvailable(String staffId, LocalDate date, LocalTime startTime, LocalTime endTime);
    }

