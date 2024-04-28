package com.example.demo.Service.impl;

import com.example.demo.Model.Reservation;
import com.example.demo.Service.ReservationService;
import com.example.demo.repo.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class ReservationServiceImpl implements ReservationService {
    private final ReservationRepository reservationRepository;

    @Autowired
    public ReservationServiceImpl(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }
    @Override
    public List<Reservation> getAllReservations() {
        return reservationRepository.findAll();
    }

    @Override
    public List<Boolean> checkAvailability(String staffId, LocalDate date) {
        List<Boolean> availability = new ArrayList<>();
        LocalTime[] timeSlotsStart = {LocalTime.parse("09:00"), LocalTime.parse("11:00"), LocalTime.parse("13:00"), LocalTime.parse("15:00"), LocalTime.parse("17:00"), LocalTime.parse("19:00")};
        LocalTime[] timeSlotsEnd = {LocalTime.parse("11:00"), LocalTime.parse("13:00"), LocalTime.parse("15:00"), LocalTime.parse("17:00"), LocalTime.parse("19:00"), LocalTime.parse("21:00")};

        boolean existsReservation = reservationRepository.existsByStaffIdAndLessonStartDate(staffId, date);
        if (!existsReservation) {
            return Arrays.asList(true, true, true, true, true, true);
        }

        for (int i = 0; i < timeSlotsStart.length; i++) {
            boolean timeOverlap = reservationRepository.existsByStaffIdAndTimeOverlap(staffId, date, timeSlotsStart[i], timeSlotsEnd[i]);
            if (!timeOverlap) {
                availability.add(true);
            } else {
                boolean isCancelled = reservationRepository.isLatestReservationCancelled(staffId, date, timeSlotsStart[i], timeSlotsEnd[i]);
                availability.add(isCancelled);
            }
        }
        return availability;
    }

    @Override
    public List<Reservation> getAllReservationsByStaffId(String staffId) {
        return reservationRepository.findByStaffId(staffId);
    }

    @Override
    public boolean hasExistingReservation(String memberId, LocalDate date, LocalTime startTime, LocalTime endTime) {
        return reservationRepository.existsByMemberIdAndLessonStartDateAndLessonStartTimeAndLessonEndTime(memberId,date, startTime, endTime);
    }

}

