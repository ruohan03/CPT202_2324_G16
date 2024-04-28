package com.example.demo.repo;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;

import java.time.LocalDate;
import java.time.LocalTime;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ReservationRepositoryTest {

    @Autowired
    private ReservationRepository reservationRepository;

    @Test
    public void testIsCoachAvailable() {
        // 测试教练T001在2023-02-24的9:00到11:00是否有预约
        LocalDate date = LocalDate.parse("2023-02-24");
        LocalTime startTime = LocalTime.parse("13:00");
        LocalTime endTime = LocalTime.parse("15:00");
        boolean haveClass = reservationRepository.existsByStaffIdAndTimeOverlap("T001", date, startTime, endTime);
        assertFalse(haveClass);  // 应该返回false，因为T001在这个时间有未取消的预约
    }

    @Test
    public void testHasRecord(){
        LocalDate date = LocalDate.parse("2023-02-24");
        boolean hasRecord = reservationRepository.existsByStaffIdAndLessonStartDate("T001", date);
        assertTrue(hasRecord);  // 应该返回false，因为T001在这个时间没有预约
    }

    @Test
    public void testNoRecord(){
        LocalDate date = LocalDate.parse("2023-02-25");
        boolean hasRecord = reservationRepository.existsByStaffIdAndLessonStartDate("T001", date);
        assertFalse(hasRecord);  // 应该返回false，因为T001在这个时间没有预约
    }

    @Test
    public void testTimeOverlap(){
        LocalDate date = LocalDate.parse("2023-02-24");
        LocalTime startTime = LocalTime.parse("09:00");
        LocalTime endTime = LocalTime.parse("11:00");
        boolean timeOverlap = reservationRepository.existsByStaffIdAndTimeOverlap("T001", date, startTime, endTime);
        assertTrue(timeOverlap);  // 应该返回true，因为T001在这个时间有未取消的预约
    }

    @Test
    public void testNoTimeOverlap(){
        LocalDate date = LocalDate.parse("2023-02-24");
        LocalTime startTime = LocalTime.parse("13:00");
        LocalTime endTime = LocalTime.parse("15:00");
        boolean timeOverlap = reservationRepository.existsByStaffIdAndTimeOverlap("T001", date, startTime, endTime);
        assertFalse(timeOverlap);  // 应该返回false，因为T001在这个时间没有预约
    }

    @Test
    public void testLatestReservationNotCancelled(){
        LocalDate date = LocalDate.parse("2024-05-03");
        LocalTime startTime = LocalTime.parse("09:00");
        LocalTime endTime = LocalTime.parse("11:00");
        boolean isCancelled = reservationRepository.isLatestReservationCancelled("T001", date, startTime, endTime);
        assertFalse(isCancelled);  // 应该返回false，因为T001在这个时间有未取消的预约
    }

    @Test
    public void testLatestReservationCancelled(){
        LocalDate date = LocalDate.parse("2024-04-30");
        LocalTime startTime = LocalTime.parse("19:00");
        LocalTime endTime = LocalTime.parse("21:00");
        boolean isCancelled = reservationRepository.isLatestReservationCancelled("T003", date, startTime, endTime);
        assertTrue(isCancelled);  // 应该返回true，因为T001在这个时间有取消的预约
    }

    @Test
    public void testReservationExist(){
        LocalDate date = LocalDate.parse("2023-02-24");
        LocalTime startTime = LocalTime.parse("09:00");
        LocalTime endTime = LocalTime.parse("11:00");
        boolean exist = reservationRepository.existsByMemberIdAndLessonStartDateAndLessonStartTimeAndLessonEndTime("M001", date, startTime, endTime);
        assertTrue(exist);

    }
}

