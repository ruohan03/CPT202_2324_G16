package com.example.demo.Controller;

import com.example.demo.Model.Course;
import com.example.demo.Model.Member;
import com.example.demo.Model.Staff;
import com.example.demo.Service.CoachService;
import com.example.demo.Service.CourseService;
import com.example.demo.Service.MemberService;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class BookingController {

    private static final Logger logger = LoggerFactory.getLogger(BookingController.class);

    @Autowired
    private CourseService courseService;

    @Autowired
    private CoachService coachService;

    @Autowired
    private MemberService memberService; // 假设存在一个会员服务来获取会员数据

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
            List<Staff> coaches = coachService.findCoachesByCourseAndLevel(courseId, maxLevel); // 根据课程和级别过滤教练
            model.addAttribute("currentPage", "bookCourse");
            model.addAttribute("isLoggedIn", true); // 始终设为 true 因为已验证用户登录
            model.addAttribute("member", member); // 添加 member 对象
            model.addAttribute("coaches", coaches);
            model.addAttribute("selectedCourse", course);
            logger.debug("Coaches displayed for course ID: {}, member plan: {}", courseId, member.getPlan());
            return "trainer-order"; // 返回选择教练的页面
        }
        logger.debug("Course ID not found, redirecting to courses page.");
        return "redirect:/member/order/course"; // 如果课程ID无效，重定向回课程列表页面
    }

    // 根据会员的计划返回可预约的教练的最高等级
    private int getMaxLevelForMemberPlan(String plan) {
        switch (plan.toLowerCase()) {
            case "gold":
                return 4;
            case "silver":
                return 3;
            case "diamond":
                return 5;
            default:
                return 0; // 默认没有访问权限
        }
    }
}
