package com.example.demo.repo;
import com.example.demo.Model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

    @Repository
    public interface ReservationRepository extends JpaRepository<Reservation,String> {

        @Query("SELECT CASE WHEN COUNT(r) > 0 THEN true ELSE false END FROM Reservation r WHERE r.staff_id = :staffId AND r.isCancelled = false AND r.lessonStartDate = :date AND ((r.lessonStartTime < :endTime AND r.lessonEndTime > :startTime) OR (r.lessonStartTime <= :startTime AND r.lessonEndTime >= :endTime))")
        boolean existsByStaffIdAndNotCancelledAndTimeOverlap(@Param("staffId") String staffId, @Param("date") LocalDate date, @Param("startTime") LocalTime startTime, @Param("endTime") LocalTime endTime);


    }

