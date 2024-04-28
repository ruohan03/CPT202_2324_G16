package com.example.demo.service;

import com.example.demo.Service.ReservationService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class reservationServiceTest {

    @Autowired
    private ReservationService reservationService;

    @Test
    public void testAvailabilityNoReservations() {
        LocalDate date = LocalDate.parse("2024-06-01");
        String staffId = "T001";
        List<Boolean> expectedAvailability = Arrays.asList(true, true, true, true, true, true);
        List<Boolean> actualAvailability = reservationService.checkAvailability(staffId, date);
        assertEquals(expectedAvailability, actualAvailability);
    }

    @Test
    public void testAvailabilityAllCancelled() {
        LocalDate date = LocalDate.parse("2024-05-03");
        String staffId = "T001";
        List<Boolean> expectedAvailability = Arrays.asList(false, true, true, true, true, true);  // 假设所有预约都被取消
        List<Boolean> actualAvailability = reservationService.checkAvailability(staffId, date);
        assertEquals(expectedAvailability, actualAvailability);
    }

    @Test
    public void testAvailabilityMixedReservations() {
        LocalDate date = LocalDate.parse("2024-04-30");
        String staffId = "T003";
        List<Boolean> expectedAvailability = Arrays.asList(true, false, true, true, true, false);  //
        List<Boolean> actualAvailability = reservationService.checkAvailability(staffId, date);
        assertEquals(expectedAvailability, actualAvailability);
    }

    @Test
    public void testAvailabilityNoReservation() {
        LocalDate date = LocalDate.parse("2024-04-30");
        String staffId = "T003";
        List<Boolean> expectedAvailability = Arrays.asList(true,false,true,true,true,false);  // 假设所有预约都被预约
        List<Boolean> actualAvailability = reservationService.checkAvailability(staffId, date);
        assertEquals(expectedAvailability, actualAvailability);
    }
}
