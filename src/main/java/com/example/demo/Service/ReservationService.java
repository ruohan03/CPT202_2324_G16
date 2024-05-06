package com.example.demo.Service;
import com.example.demo.Model.Reservation;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;


public interface ReservationService {

    List<Reservation> getAllReservations();

    List<Boolean> checkAvailability(String staffId, LocalDate date);

    List<Reservation> getAllReservationsByStaffId(String staffId);

    List<Reservation> getIncompleteReservationsByStaffId(String staffId);

    List<Reservation> getCompleteReservationsByStaffId(String staffId);

    boolean hasExistingReservation(String memberId, LocalDate date, LocalTime startTime, LocalTime endTime);

    boolean createReservation(Reservation reservation);

    boolean checkAvailabilityForNewReservation(String memberId, LocalDate date, LocalTime startTime, LocalTime endTime);

    List<Reservation> getCompletedReservationsForMember(String memberId);
    List<Reservation> getIncompleteReservationsForMember(String memberId);

    String cancelReservation(String reservationId, String memberId);
    String signIn(String reservationId, String memberId);
    List<Reservation> findReservationsByDate(String memberId, LocalDate date);

    boolean signInReservation(String reservationId);

    List<Reservation> findReservationByMemberId(String memberId);

    int getThisWeekReservationsCount();

    int getThisWeekReservationsMembersCount();

    int getLastWeekReservationsCount();

    int getLastWeekReservationsMembersCount();

    //获取已上课、未上课、缺席和取消的预约数量
    int getAttendedReservationsCount();

    int getNotAttendedYetReservationsCount();

    int getMissedReservationsCount();

    int getCancelledReservationsCount();

    //教练周业绩
    List<Object[]> getWeeklyReservationsCountForStaff();
}

