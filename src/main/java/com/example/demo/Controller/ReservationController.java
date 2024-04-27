package com.example.demo.Controller;
import com.example.demo.Model.Purchase;
import com.example.demo.Model.Staff;
import com.example.demo.Service.ReservationService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import com.example.demo.Service.StaffService;
import com.example.demo.Model.Reservation;

import java.lang.constant.Constable;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;


@Controller
public class ReservationController {

    private final ReservationService reservationService;

    @Autowired
    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping("/reservation-list")
    public String viewReservations(Model model) {
        // 调用服务层获取预约信息列表
        model.addAttribute("reservationList", reservationService.getAllReservations());
        return "reservation-list";
    }

    @Autowired
    private StaffService staffService;


    @GetMapping("/view_schedule")
    public String getStaffReservations(HttpSession session, Model model){
        String staffId = (String) session.getAttribute("staffId");
        if (staffId != null) {
            model.addAttribute("staffReservationList", reservationService.getAllReservationsByStaffId(staffId));
            return"view_schedule";
        }
        else {
            return "redirect:/staff/login";
        }
    }
}

