package com.example.demo.Controller;
import com.example.demo.Model.*;
import com.example.demo.Service.ReservationService;
import com.example.demo.Service.CourseService;
import com.example.demo.Service.MemberService;
import com.example.demo.Service.TrainerService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.example.demo.Service.StaffService;

import java.time.LocalTime;
import java.util.List;

import java.time.LocalDate;


@Controller
public class ReservationController {

    private final ReservationService reservationService;
    private final CourseService courseService;
    private final MemberService memberService;
    private final TrainerService trainerService;
    private final StaffService staffService;

    private static final Logger logger = LoggerFactory.getLogger(ReservationController.class);

    @Autowired
    public ReservationController(ReservationService reservationService,
                                 CourseService courseService, MemberService memberService,
                                 TrainerService trainerService, StaffService staffService) {
        this.reservationService = reservationService;
        this.courseService = courseService;
        this.memberService = memberService;
        this.trainerService = trainerService;
        this.staffService = staffService;
    }

    @GetMapping("/reservation-list")
    public String viewReservations(Model model) {
        // 调用服务层获取预约信息列表
        model.addAttribute("reservationList", reservationService.getAllReservations());
        return "reservation-list";
    }

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

    // 确保在你的控制器中导入相关类，比如 List, CourseService, Course等
    @GetMapping("/api/courses")
    @ResponseBody
    public List<Course> getCourses() {
        return courseService.findAllCourses();
    }


    @GetMapping("/member/order/course")
    public String listCourses(Model model, HttpSession session) {
        logger.debug("Checking login status for accessing courses.");

        // 获取会话中的 memberId，判断登录状态
        String memberId = (String) session.getAttribute("memberId");
        if (memberId == null) {
            logger.debug("No memberId found in session, redirecting to login.");
            return "redirect:/login"; // 如果未登录，重定向到登录页面
        }

        // 从数据库获取 member 对象
        Member member = memberService.findMemberById(memberId);
        if (member == null) {
            logger.debug("No member found with the provided memberId, redirecting to login.");
            return "redirect:/login"; // 如果找不到成员信息，也重定向到登录页面
        }

        // 添加属性到模型中，供前端使用
        model.addAttribute("currentPage", "bookCourse");
        model.addAttribute("isLoggedIn", true); // 始终设为 true 因为已验证用户登录
        model.addAttribute("member", member); // 添加 member 对象

        // 获取课程列表并添加到模型中
        List<Course> courses = courseService.findAllCourses();
        model.addAttribute("courses", courses);
        logger.debug("Courses listed for logged-in user.");

        return "course-order"; // 返回课程列表页面
    }


    @GetMapping("/select-coach/{courseId}")
    public String selectCoach(@PathVariable String courseId, Model model, HttpSession session) {
        logger.debug("Selecting coach for course ID: {}", courseId);
        String memberId = (String) session.getAttribute("memberId");
        if (memberId == null) {
            logger.debug("No memberId in session, redirecting to login.");
            return "redirect:/login"; // 如果未登录，重定向到登录页面
        }

        Member member = memberService.findMemberById(memberId); // 获取当前登录会员信息
        if (member == null) {
            logger.debug("Member data not found, redirecting to courses page.");
            return "redirect:/member/order/course"; // 如果会员信息不存在，重定向回课程列表
        }

        Course course = courseService.findCourseById(courseId);
        if (course != null) {
            int maxLevel = getMaxLevelForMemberPlan(member.getPlan()); // 获取会员计划允许的最高教练级别
            List<Staff> trainer = trainerService.findCoachesByCourseAndLevel(courseId, maxLevel); // 根据课程和级别过滤教练
            model.addAttribute("currentPage", "bookCourse");
            model.addAttribute("isLoggedIn", true); // 始终设为 true 因为已验证用户登录
            model.addAttribute("member", member); // 添加 member 对象
            model.addAttribute("Trainers", trainer);
            model.addAttribute("selectedCourse", course);
            logger.debug("Trainers displayed for course ID: {}, member plan: {}", courseId, member.getPlan());
            return "trainer-order"; // 返回选择教练的页面
        }
        logger.debug("Course ID not found, redirecting to courses page.");
        return "redirect:/member/order/course"; // 如果课程ID无效，重定向回课程列表页面
    }

    // 根据会员的计划返回可预约的教练的最高等级
    private int getMaxLevelForMemberPlan(String plan) {
        return switch (plan.toLowerCase()) {
            case "gold" -> 4;
            case "silver" -> 3;
            case "diamond" -> 5;
            default -> 0; // 默认没有访问权限
        };
    }

    @GetMapping("/member/select-time/{staffId}")
    public String chooseTime(@PathVariable String staffId, Model model, HttpSession session) {
        String memberId = (String) session.getAttribute("memberId");
        if (memberId == null) {
            return "redirect:/login";
        }

        Member member = memberService.findMemberById(memberId);
        if (member == null) {
            return "redirect:/login";
        }
        model.addAttribute("member", memberService.findMemberById(memberId));
        model.addAttribute("currentPage", "bookCourse");
        model.addAttribute("isLoggedIn", true); // 始终设为 true 因为已验证用户登录
        model.addAttribute("staffId", staffId);
        return "time-selection";
    }

    @GetMapping("/api/check-time/{staffId}")
    @ResponseBody
    public List<Boolean> checkTime(@PathVariable String staffId, @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return reservationService.checkAvailability(staffId, date);
    }

    @GetMapping("/api/has-reservation/{memberId}")
    @ResponseBody
    public boolean checkForExistingReservation(
            @PathVariable String memberId,
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam("startTime") @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime startTime,
            @RequestParam("endTime") @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime endTime) {

        return reservationService.hasExistingReservation(memberId, date, startTime, endTime);
    }

}