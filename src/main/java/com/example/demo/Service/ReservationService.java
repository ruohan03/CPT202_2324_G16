package com.example.demo.Service;
import com.example.demo.Model.Reservation;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;


public interface ReservationService {

    List<Reservation> getAllReservations();

    List<Boolean> checkAvailability(String staffId, LocalDate date);

    List<Reservation> getAllReservationsByStaffId(String staffId);

    boolean hasExistingReservation(String memberId, LocalDate date, LocalTime startTime, LocalTime endTime);

    boolean createReservation(Reservation reservation);

    boolean checkAvailabilityForNewReservation(String memberId, LocalDate date, LocalTime startTime, LocalTime endTime);

    List<Reservation> getCompletedReservationsForMember(String memberId);
    List<Reservation> getIncompleteReservationsForMember(String memberId);

    String cancelReservation(String reservationId, String memberId);
    String signIn(String reservationId, String memberId);
    List<Reservation> findReservationsByDate(String memberId, LocalDate date);



}

