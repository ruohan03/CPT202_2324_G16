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

    boolean existsByStaffIdAndLessonStartDate(@Param("staffId") String staffId, @Param("date") LocalDate date);

    @Query("SELECT CASE WHEN COUNT(r) > 0 THEN true ELSE false END FROM Reservation r WHERE r.staffId = :staffId AND r.lessonStartDate = :date AND ((r.lessonStartTime < :endTime AND r.lessonEndTime > :startTime) OR (r.lessonStartTime <= :startTime AND r.lessonEndTime >= :endTime))")
    boolean existsByStaffIdAndTimeOverlap(@Param("staffId") String staffId, @Param("date") LocalDate date, @Param("startTime") LocalTime startTime, @Param("endTime") LocalTime endTime);

    @Query("SELECT CASE WHEN r.isCancelled = true THEN true ELSE false END FROM Reservation r WHERE r.staffId = :staffId AND r.lessonStartDate = :date AND r.lessonStartTime = :startTime AND r.lessonEndTime = :endTime AND r.createdDate = (SELECT MAX(rr.createdDate) FROM Reservation rr WHERE rr.staffId = r.staffId AND rr.lessonStartDate = r.lessonStartDate AND rr.lessonStartTime = r.lessonStartTime AND rr.lessonEndTime = r.lessonEndTime)")
    boolean isLatestReservationCancelled(@Param("staffId") String staffId, @Param("date") LocalDate date, @Param("startTime") LocalTime startTime, @Param("endTime") LocalTime endTime);

    List<Reservation> findByStaffId(String staffId);

    boolean existsByMemberIdAndLessonStartDateAndLessonStartTimeAndLessonEndTime(String memberId, LocalDate date, LocalTime startTime, LocalTime endTime);

    @Query("SELECT r FROM Reservation r WHERE r.staffId = :staffId AND r.lessonStartDate = :date AND " +
            "r.lessonStartTime < :endTime AND r.lessonEndTime > :startTime AND r.isCancelled = false")
    List<Reservation> findOverlappingReservations(String staffId, LocalDate date, LocalTime startTime, LocalTime endTime);

    List<Reservation> findByMemberIdAndIsCancelledFalseAndIsSignInTrue(String memberId);

    // 查询未来的预约
    List<Reservation> findByMemberIdAndIsCancelledFalseAndLessonStartDateGreaterThanEqualAndLessonStartTimeGreaterThanEqual(String memberId, LocalDate date, LocalTime time);

    // 查询过去但未签到的预约
    List<Reservation> findByMemberIdAndIsCancelledFalseAndIsSignInFalseAndLessonStartDateLessThanEqualAndLessonStartTimeLessThanEqual(String memberId, LocalDate date, LocalTime time);
    List<Reservation> findByMemberIdAndLessonStartDate(String memberId, LocalDate lessonStartDate);
    // 查询未取消的预约
    List<Reservation> findByMemberIdAndIsCancelledFalse(String memberId);

    // 根据日期查询未取消的预约
    List<Reservation> findByMemberIdAndIsCancelledFalseAndLessonStartDate(String memberId, LocalDate date);

    List<Reservation> findReservationByMemberId(String memberId);

    List<Reservation> findByStaffIdAndIsCancelledFalseAndIsSignInFalse(String staffId);

    List<Reservation> findByStaffIdAndIsCancelledFalseAndIsSignInTrue(String staffId);

    boolean existsByMemberIdAndLessonStartDateAndLessonStartTimeAndLessonEndTimeAndIsCancelled(
            String memberId, LocalDate date, LocalTime startTime, LocalTime endTime, boolean isCancelled);

    @Query("SELECT COUNT(r) FROM Reservation r WHERE r.lessonStartDate BETWEEN :start AND :end")
    int countByDateBetween(@Param("start") LocalDate startOfWeek, @Param("end") LocalDate endOfWeek);

    @Query("SELECT COUNT(DISTINCT r.memberId) FROM Reservation r WHERE r.lessonStartDate BETWEEN :start AND :end")
    int countDistinctMemberByDateBetween(@Param("start") LocalDate startOfWeek, @Param("end") LocalDate endOfWeek);

//    获取已上课、未上课、缺席和取消的预约数量
    @Query("SELECT COUNT(r) FROM Reservation r WHERE r.isSignIn = true AND r.isCancelled = false AND r.lessonStartDate BETWEEN :start AND :end")
    int countByAttended(@Param("start") LocalDate startOfWeek, @Param("end") LocalDate endOfWeek);

    @Query("SELECT COUNT(r) FROM Reservation r WHERE r.isSignIn = false AND r.isCancelled = false AND r.lessonStartTime > :now AND r.lessonStartDate BETWEEN :start AND :end")
    int countByNotAttendedYet(@Param("now") LocalTime now, @Param("start") LocalDate startOfWeek, @Param("end") LocalDate endOfWeek);

    @Query("SELECT COUNT(r) FROM Reservation r WHERE r.isSignIn = false AND r.isCancelled = false AND r.lessonStartTime < :now AND r.lessonStartDate BETWEEN :start AND :end")
    int countByMissed(@Param("now") LocalTime now, @Param("start") LocalDate startOfWeek, @Param("end") LocalDate endOfWeek);

    @Query("SELECT COUNT(r) FROM Reservation r WHERE r.isCancelled = true AND r.lessonStartDate BETWEEN :start AND :end")
    int countByCancelled(@Param("start") LocalDate startOfWeek, @Param("end") LocalDate endOfWeek);

    //教练周业绩
    @Query("SELECT r.staffId, COUNT(r) FROM Reservation r WHERE r.lessonStartDate BETWEEN :start AND :end AND r.isCancelled = false GROUP BY r.staffId ORDER BY COUNT(r) DESC")
    List<Object[]> countByStaffIdAndDateBetweenAndIsCancelledFalse(@Param("start") LocalDate start, @Param("end") LocalDate end);

    @Query("SELECT DISTINCT r.staffId FROM Reservation r WHERE r.lessonStartDate BETWEEN :start AND :end")
    List<String> findDistinctStaffIdByDateBetween(@Param("start") LocalDate start, @Param("end") LocalDate end);
}

