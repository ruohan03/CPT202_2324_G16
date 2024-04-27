package com.example.demo.repo;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.format.annotation.DateTimeFormat;

import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.time.LocalTime;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ReservationRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ReservationRepository reservationRepository;

    @Test
    public void testIsCoachAvailable() {
        // 测试教练T001在2023-02-24的9:00到11:00是否有预约
        LocalDate date = LocalDate.parse("2023-02-24");
        LocalTime startTime = LocalTime.parse("09:00");
        LocalTime endTime = LocalTime.parse("11:00");
        boolean haveClass = reservationRepository.existsByStaffIdAndNotCancelledAndTimeOverlap("T001", date, startTime, endTime);
        assertFalse(haveClass);  // 应该返回false，因为T001在这个时间有未取消的预约

        // 测试一个无冲突时间
        startTime = LocalTime.parse("13:00");
        endTime = LocalTime.parse("15:00");
        haveClass = reservationRepository.existsByStaffIdAndNotCancelledAndTimeOverlap("T001", date, startTime, endTime);
        assertFalse(haveClass);  // 应该返回true，因为T001在这个时间没有预约
    }
}

