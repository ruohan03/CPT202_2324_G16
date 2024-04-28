package com.example.demo.repo;
import com.example.demo.Model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
    public interface ReservationRepository extends JpaRepository<Reservation,String> {

    boolean existsByStaffIdAndLessonStartDate(@Param("staffId") String staffId, @Param("date") LocalDate date);

    @Query("SELECT CASE WHEN COUNT(r) > 0 THEN true ELSE false END FROM Reservation r WHERE r.staffId = :staffId AND r.lessonStartDate = :date AND ((r.lessonStartTime < :endTime AND r.lessonEndTime > :startTime) OR (r.lessonStartTime <= :startTime AND r.lessonEndTime >= :endTime))")
    boolean existsByStaffIdAndTimeOverlap(@Param("staffId") String staffId, @Param("date") LocalDate date, @Param("startTime") LocalTime startTime, @Param("endTime") LocalTime endTime);

    @Query("SELECT CASE WHEN r.isCancelled = true THEN true ELSE false END FROM Reservation r WHERE r.staffId = :staffId AND r.lessonStartDate = :date AND r.lessonStartTime = :startTime AND r.lessonEndTime = :endTime AND r.createdDate = (SELECT MAX(rr.createdDate) FROM Reservation rr WHERE rr.staffId = r.staffId AND rr.lessonStartDate = r.lessonStartDate AND rr.lessonStartTime = r.lessonStartTime AND rr.lessonEndTime = r.lessonEndTime)")
    boolean isLatestReservationCancelled(@Param("staffId") String staffId, @Param("date") LocalDate date, @Param("startTime") LocalTime startTime, @Param("endTime") LocalTime endTime);

    List<Reservation> findByStaffId(String staffId);

    boolean existsByMemberIdAndLessonStartDateAndLessonStartTimeAndLessonEndTime(String memberId, LocalDate date, LocalTime startTime, LocalTime endTime);
}

