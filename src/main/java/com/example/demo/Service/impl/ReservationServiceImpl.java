package com.example.demo.Service.impl;

import com.example.demo.Model.Reservation;
import com.example.demo.Service.ReservationService;
import com.example.demo.repo.ReservationRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

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
    public List<Reservation> getIncompleteReservationsByStaffId(String staffId) {
        return reservationRepository.findByStaffIdAndIsCancelledFalseAndIsSignInFalse(staffId);
    }

    @Override
    public List<Reservation> getCompleteReservationsByStaffId(String staffId) {
        return reservationRepository.findByStaffIdAndIsCancelledFalseAndIsSignInTrue(staffId);
    }

    @Override
    public boolean hasExistingReservation(String memberId, LocalDate date, LocalTime startTime, LocalTime endTime) {
        // 先检查是否存在未取消的预约
        return reservationRepository.existsByMemberIdAndLessonStartDateAndLessonStartTimeAndLessonEndTimeAndIsCancelled(
                memberId, date, startTime, endTime, false);
    }


    @Transactional
    @Override
    public boolean createReservation(Reservation reservation) {
        try {
            reservationRepository.save(reservation);
            return true; // 表示保存成功
        } catch (Exception e) {
            e.printStackTrace();
            return false; // 表示保存失败
        }
    }

    @Override
    public boolean checkAvailabilityForNewReservation(String staffId, LocalDate date, LocalTime startTime, LocalTime endTime) {
        // Find overlapping reservations that are not cancelled.
        List<Reservation> overlappingReservations = reservationRepository.findOverlappingReservations(staffId, date, startTime, endTime);
        return overlappingReservations.isEmpty();
    }

    @Override
    public List<Reservation> getCompletedReservationsForMember(String memberId) {
        return reservationRepository.findByMemberIdAndIsCancelledFalseAndIsSignInTrue(memberId);
    }

    @Override
    public List<Reservation> getIncompleteReservationsForMember(String memberId) {
        LocalDate today = LocalDate.now();
        LocalTime now = LocalTime.now();

        List<Reservation> allReservations = reservationRepository.findReservationByMemberId(memberId);

        List<Reservation> futureReservations = allReservations.stream()
                .filter(r -> r.getLessonStartDate().isAfter(today) ||
                        (r.getLessonStartDate().equals(today) && r.getLessonStartTime().isAfter(now)))
                .collect(Collectors.toList());

        List<Reservation> missedReservations = allReservations.stream()
                .filter(r -> !r.getIsSignIn() &&
                        (r.getLessonStartDate().isBefore(today) ||
                                (r.getLessonStartDate().equals(today) && r.getLessonStartTime().isBefore(now))))
                .collect(Collectors.toList());

        futureReservations.addAll(missedReservations);

        return futureReservations;
    }



    public String cancelReservation(String reservationId, String memberId) {
        Optional<Reservation> reservationOpt = reservationRepository.findById(reservationId);
        if (reservationOpt.isPresent()) {
            Reservation reservation = reservationOpt.get();
            if (reservation.getMemberId().equals(memberId) && !reservation.getIsCancelled() && LocalDate.now().isBefore(reservation.getLessonStartDate())) {
                reservation.setIsCancelled(true);
                reservationRepository.save(reservation);
                return "预约已取消";
            }
        }
        return "无法取消预约";
    }

    public String signIn(String reservationId, String memberId) {
        Optional<Reservation> reservationOpt = reservationRepository.findById(reservationId);
        if (reservationOpt.isPresent()) {
            Reservation reservation = reservationOpt.get();
            if (reservation.getMemberId().equals(memberId) && !reservation.getIsSignIn() && LocalDate.now().equals(reservation.getLessonStartDate()) && LocalTime.now().isAfter(reservation.getLessonStartTime())&&LocalTime.now().isBefore(reservation.getLessonEndTime())) {
                reservation.setIsSignIn(true);
                reservationRepository.save(reservation);
                return "签到成功";
            }
        }
        return "无法签到";
    }

    public List<Reservation> findReservationsByDate(String memberId, LocalDate date) {
        return reservationRepository.findByMemberIdAndLessonStartDate(memberId, date);
    }

    @Override
    public boolean signInReservation(String reservationId) {
        Optional<Reservation> reservation = reservationRepository.findById(reservationId);
        if (reservation.isPresent() && !reservation.get().getIsSignIn()) {
            Reservation updatedReservation = reservation.get();
            updatedReservation.setSignIn(true);
            reservationRepository.save(updatedReservation);
            return true;
        }
        return false;
    }

}

