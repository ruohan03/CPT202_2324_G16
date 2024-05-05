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
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import com.example.demo.Service.StaffService;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;

import java.time.LocalDate;
import java.util.Map;


@Controller
@SessionAttributes({"courseId", "staffId"})
public class ReservationController {

    private final ReservationService reservationService;
    private final CourseService courseService;
    private final MemberService memberService;
    private final TrainerService trainerService;
    private final StaffService staffService;

    private static final Logger logger = LoggerFactory.getLogger(ReservationController.class);

    private Member validateMember(HttpSession session, Model model) {
        String memberId = (String) session.getAttribute("memberId");
        if (memberId == null) {
            logger.debug("No memberId in session, redirecting to login.");
            model.addAttribute("redirectReason", "Not logged in");
            return null;
        }
        Member member = memberService.findMemberById(memberId);
        if (member == null) {
            logger.debug("No member found with the provided memberId, redirecting to login.");
            model.addAttribute("redirectReason", "Member not found");
        }
        return member;
    }

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
            model.addAttribute("staffReservationList", reservationService.getIncompleteReservationsByStaffId(staffId));
            return"view_schedule";
        }
        else {
            return "redirect:/staff/login";
        }
    }


    @PostMapping("/sign_in/{reservationId}")
    public String signIn(@PathVariable("reservationId") String reservationId, HttpSession session) {
        String staffId = (String) session.getAttribute("staffId");
        if (staffId != null && reservationService.signInReservation(reservationId)) {
            return "redirect:/view_schedule";
        } else {
            return "redirect:/staff/login";
        }
    }



    @GetMapping("/view_all_schedule")
    public String getAllStaffReservations(HttpSession session, Model model){
        String staffId = (String) session.getAttribute("staffId");
        if (staffId != null) {
            model.addAttribute("staffAllReservationList", reservationService.getAllReservationsByStaffId(staffId));
            return"view_all_schedule";
        }
        else {
            return "redirect:view_schedule";
        }
    }

    @GetMapping("/view_complete_schedule")
    public String getCompleteStaffReservations(HttpSession session, Model model){
        String staffId = (String) session.getAttribute("staffId");
        if (staffId != null) {
            model.addAttribute("staffCompleteReservationList", reservationService.getCompleteReservationsByStaffId(staffId));
            return"view_complete_schedule";
        }
        else {
            return "redirect:view_schedule";
        }
    }


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
            return "redirect:/login"; // 如果找不到成员信息，也重定向到登录页面
        }

        // 获取课程列表并检查每个课程的激活状态
        List<Course> courses = courseService.findAllCourses();
        courses.forEach(course -> course.setActive(courseService.checkCourseActive(course))); // 检查并设置激活状态

        // 添加属性到模型中，供前端使用
        model.addAttribute("currentPage", "bookCourse");
        model.addAttribute("isLoggedIn", true); // 始终设为 true 因为已验证用户登录
        model.addAttribute("member", member); // 添加 member 对象
        model.addAttribute("courses", courses); // 添加处理过的课程列表

        logger.debug("Courses listed for logged-in user with active status.");

        return "reserve-course"; // 返回课程列表页面
    }



    @GetMapping("/member/select-trainer")
    public String selectCoach(@RequestParam("courseId") String courseId, Model model, HttpSession session) {
        logger.debug("Selecting coach for course ID: {}", courseId);
        session.setAttribute("courseId", courseId);
        String memberId = (String) session.getAttribute("memberId");
        if (memberId == null) {
            logger.debug("No memberId in session, redirecting to login.");
            return "redirect:/login"; // Redirect to login if not logged in
        }

        Member member = memberService.findMemberById(memberId);
        if (member == null) {
            logger.debug("Member data not found, redirecting to login.");
            return "redirect:/login"; // Redirect if member is not found
        }

        Course course = courseService.findCourseById(courseId);
        if (course == null) {
            logger.debug("Course ID not found, redirecting to courses page.");
            return "redirect:/member/order/course"; // Redirect if course is not found
        }

        int maxLevel = getMaxLevelForMemberPlan(member.getPlan());
        List<Staff> trainers = trainerService.findCoachesByCourseAndLevel(courseId, maxLevel);
        model.addAttribute("currentPage", "bookCourse");
        model.addAttribute("isLoggedIn", true);
        model.addAttribute("member", member);
        model.addAttribute("Trainers", trainers);
        model.addAttribute("selectedCourse", course);

        logger.debug("Trainers displayed for course ID: {}, member plan: {}", courseId, member.getPlan());
        return "reserve-trainer"; // Render the trainer selection page
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

    @GetMapping("/member/select-time")
    public String chooseTime(@RequestParam("staffId") String staffId,
                             @RequestParam("courseId") String courseId,
                             Model model,
                             HttpSession session) {
        String memberId = (String) session.getAttribute("memberId");
        if (memberId == null) {
            return "redirect:/login";
        }

        Member member = memberService.findMemberById(memberId);
        if (member == null) {
            return "redirect:/login";
        }
        session.setAttribute("staffId", staffId);
        model.addAttribute("member", memberService.findMemberById(memberId));
        model.addAttribute("currentPage", "bookCourse");
        model.addAttribute("isLoggedIn", true); // 始终设为 true 因为已验证用户登录
        model.addAttribute("staffId", staffId);
        model.addAttribute("selectedCourseId", courseId);
        return "reserve-time";
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

    @GetMapping("/member/confirm-reservation")
    public String confirmReservation(@RequestParam("staffId") String staffId,
                                     @RequestParam("courseId") String courseId,
                                     @RequestParam("date") String date,
                                     @RequestParam("time") String time, Model model, HttpSession session) {
        if (session.getAttribute("memberId") == null) {
            return "redirect:/login";
        }

        Member member = memberService.findMemberById((String) session.getAttribute("memberId"));
        Course course = courseService.findCourseById(courseId);
        Staff staff = staffService.findStaffById(staffId);

        String[] timeParts = time.split("-");
        String startTime = timeParts[0];
        String endTime = timeParts[1];

        model.addAttribute("member", member);
        model.addAttribute("course", course);
        model.addAttribute("staff", staff);
        model.addAttribute("date", date);
        model.addAttribute("startTime", startTime);
        model.addAttribute("endTime", endTime);
        model.addAttribute("currentPage", "bookCourse");
        model.addAttribute("isLoggedIn", true);

        return "reserve-confirm";
    }

    @PostMapping("/member/finalize-reservation")
    @ResponseBody
    public Map<String, Object> finalizeReservation(
            @RequestParam("date") String date,
            @RequestParam("time") String time,
            @ModelAttribute Reservation reservation,
            BindingResult bindingResult,
            HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        // 检查绑定错误和其他必要的验证
        if (bindingResult.hasErrors()) {
            // 处理错误
            response.put("result", "error");
            response.put("message", "The form was filled in incorrectly. Please try again.");
            return response;
        }

        String memberId = (String) session.getAttribute("memberId");
        if (memberId == null) {
            response.put("result", "error");
            response.put("message", "Please log in before making an appointment.");
            return response;
        }

        LocalDate lessonDate = LocalDate.parse(date);
        String[] times = time.split("-");
        LocalTime lessonStartTime = LocalTime.parse(times[0]);
        LocalTime lessonEndTime = LocalTime.parse(times[1]);

        reservation.setMemberId(memberId);
        reservation.setLessonStartDate(lessonDate);
        reservation.setLessonStartTime(lessonStartTime);
        reservation.setLessonEndTime(lessonEndTime);
        reservation.setCreatedDate(LocalDateTime.now());

        boolean reservationSaved = reservationService.createReservation(reservation);
        if (reservationSaved) {
            response.put("result", "success");
            response.put("message", "The appointment was successful!");
        } else {
            response.put("result", "error");
            response.put("message", "Reservation failed. Please try again!");
        }

        return response;
    }

    @GetMapping("/member/all-reservations")
    public String getAllReservationsForUser(HttpSession session, Model model) {
        // 获取会话中的 memberId，判断登录状态
        String memberId = (String) session.getAttribute("memberId");
        if (memberId == null) {
            logger.debug("No memberId found in session, redirecting to login.");
            return "redirect:/login"; // 如果未登录，重定向到登录页面
        }

        // 从数据库获取 member 对象
        Member member = memberService.findMemberById(memberId);
        if (member == null) {
            return "redirect:/login"; // 如果找不到成员信息，也重定向到登录页面
        }

        // 获取预约列表
        List<Reservation> reservations = reservationService.findReservationByMemberId(memberId);

        // 获取每个预约对应的 staff 对象
        Map<String, Staff> staffMap = new HashMap<>();
        Map<String, Course> courseMap = new HashMap<>();
        for (Reservation reservation : reservations) {
            Staff staff = staffService.findStaffById(reservation.getStaffId());
            Course course = courseService.findCourseById(reservation.getCourseId());
            staffMap.put(reservation.getReservationId(), staff);
            courseMap.put(reservation.getReservationId(), course);
        }

        // 添加属性到模型中，供前端使用
        model.addAttribute("currentPage", "viewReservations");
        model.addAttribute("isLoggedIn", true); // 始终设为 true 因为已验证用户登录
        model.addAttribute("member", member); // 添加 member 对象
        model.addAttribute("reservations", reservations); // 添加处理过的预约列表
        model.addAttribute("staffMap", staffMap); // 添加 staff 对象的映射
        model.addAttribute("courseMap", courseMap); // 添加 course 对象的映射

        logger.debug("Reservations listed for logged-in user.");

        return "all-reservations"; // 返回预约列表页面
    }

    @GetMapping("/member/completed")
    public String getCompletedReservations(HttpSession session, Model model) {
        String memberId = (String) session.getAttribute("memberId");
        if (memberId == null) {
            return "redirect:/login";  // 如果会员未登录，重定向到登录页面
        }
        // 从数据库获取 member 对象
        Member member = memberService.findMemberById(memberId);
        if (member == null) {
            return "redirect:/login"; // 如果找不到成员信息，也重定向到登录页面
        }

        List<Reservation> completedReservations = reservationService.getCompletedReservationsForMember(memberId);
        Map<String, Staff> staffMap = new HashMap<>();
        Map<String, Course> courseMap = new HashMap<>();
        for (Reservation reservation : completedReservations) {
            Staff staff = staffService.findStaffById(reservation.getStaffId());
            Course course = courseService.findCourseById(reservation.getCourseId());
            staffMap.put(reservation.getReservationId(), staff);
            courseMap.put(reservation.getReservationId(), course);
        }
        model.addAttribute("completedReservations", completedReservations);
        model.addAttribute("staffMap", staffMap); // 添加 staff 对象的映射
        model.addAttribute("courseMap", courseMap); // 添加 course 对象的映射
        model.addAttribute("member", member);
        model.addAttribute("isLoggedIn", true);
        model.addAttribute("currentPage", "viewReservations");
        return "completed-reservations";  // 返回已完成预约的视图
    }

    // 获取未完成预约
    // 获取未完成的预约列表
    @GetMapping("/member/incomplete")
    public String getIncompleteReservations(HttpSession session, Model model, RedirectAttributes redirectAttributes) {
        String memberId = (String) session.getAttribute("memberId");
        if (memberId == null) {
            // 如果会员未登录，存储一条错误消息并重定向到登录页面
            redirectAttributes.addFlashAttribute("error", "您必须先登录才能访问预约信息。");
            return "redirect:/login";
        }
        // 从数据库获取 member 对象
        Member member = memberService.findMemberById(memberId);
        if (member == null) {
            return "redirect:/login"; // 如果找不到成员信息，也重定向到登录页面
        }
        try {
            List<Reservation> incompleteReservations = reservationService.getIncompleteReservationsForMember(memberId);
            Map<String, Staff> staffMap = new HashMap<>();
            Map<String, Course> courseMap = new HashMap<>();
            for (Reservation reservation : incompleteReservations) {
                Staff staff = staffService.findStaffById(reservation.getStaffId());
                Course course = courseService.findCourseById(reservation.getCourseId());
                staffMap.put(reservation.getReservationId(), staff);
                courseMap.put(reservation.getReservationId(), course);
            }
            model.addAttribute("incompleteReservations", incompleteReservations);
            model.addAttribute("staffMap", staffMap); // 添加 staff 对象的映射
            model.addAttribute("courseMap", courseMap); // 添加 course 对象的映射
            model.addAttribute("member", member);
            model.addAttribute("isLoggedIn", true);
            model.addAttribute("currentPage", "viewReservations");
            return "incomplete-reservations";  // 返回未完成预约的视图
        } catch (Exception e) {
            // 处理任何潜在的异常，并给出一个用户友好的错误消息
            redirectAttributes.addFlashAttribute("error", "获取预约信息时发生错误，请稍后再试。");
            return "redirect:/";  // 可以重定向到一个通用的错误页面或者首页
        }
    }

    // 取消预约
    @PostMapping("/cancel")
    public String cancelReservation(@RequestParam("reservationId") String reservationId, HttpSession session, RedirectAttributes redirectAttributes) {
        String memberId = (String) session.getAttribute("memberId");
        if (memberId == null) {
            return "redirect:/login";
        }
        String result = reservationService.cancelReservation(reservationId, memberId);
        redirectAttributes.addFlashAttribute("message", result);
        return "redirect:/incomplete";
    }

    // 签到
    @PostMapping("/signin")
    public String signIn(@RequestParam("reservationId") String reservationId, HttpSession session, RedirectAttributes redirectAttributes) {
        String memberId = (String) session.getAttribute("memberId");
        if (memberId == null) {
            return "redirect:/login";
        }
        String result = reservationService.signIn(reservationId, memberId);
        redirectAttributes.addFlashAttribute("message", result);
        return "redirect:/completed";
    }

    // 根据日期搜索未完成预约
    // 搜索预约，可选日期参数
    @GetMapping("/search")
    public String searchReservations(@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date, HttpSession session, Model model) {
        String memberId = (String) session.getAttribute("memberId");
        if (memberId == null) {
            return "redirect:/login";
        }

        List<Reservation> reservations;
        if (date != null) {
            reservations = reservationService.findReservationsByDate(memberId, date);
        } else {
            reservations = reservationService.getIncompleteReservationsForMember(memberId);
        }

        model.addAttribute("incompleteReservations", reservations);
        return "incomplete-reservations";
    }

        @GetMapping("/history")
        public String showHistory() {return "history-Reservation";
        }

    }





