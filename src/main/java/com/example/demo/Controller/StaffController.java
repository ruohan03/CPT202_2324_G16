package com.example.demo.Controller;

import com.example.demo.Model.Course;
import com.example.demo.Model.Staff;
import com.example.demo.Service.CourseService;
import com.example.demo.Service.StaffService;
import com.example.demo.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
public class StaffController {
    @Autowired
    private StaffService staffService;
    @Autowired
    private CourseService courseService;

    @GetMapping("/staff")
    public String getAllStaff(Model model){
        List<Staff> staff = staffService.findAllStaff();
        System.out.println(staff); // 用于调试，查看获取的员工信息
        model.addAttribute("staff", staff);
        return "staffView";  // 指向你的员工视图的Thymeleaf模板
    }

    @PostMapping("/addStaff")
    public String addStaff(@ModelAttribute("staff") Staff staff){
        staffService.addStaff(staff);
        return "redirect:/staff"; // 重定向到显示所有员工的页面
    }

    @GetMapping("/staff/course/{courseId}")
    public ResponseEntity<List<Staff>> getStaffByCourseId(@PathVariable String courseId) {
        try {
            List<Staff> staff = staffService.findStaffByCourseId(courseId);
            return ResponseEntity.ok().body(staff);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/courses/{courseId}/staff")
    public String getCourseStaff(@PathVariable String courseId, Model model) {
        List<Staff> staff = staffService.findStaffByCourseId(courseId);
        String courseName = courseService.findCourseNameById(courseId);
        model.addAttribute("staff", staff);
        model.addAttribute("courseName", courseName); // 假设您已经有方法获取课程名称
        return "course_staff"; // Thymeleaf 模板名称
    }

//登录

    @GetMapping("/login/staff")
    public String showLoginForm() {
        return "login_staff";
    }
    @PostMapping("/login/staff")
    public String login(@RequestParam String phone, @RequestParam String password, Model model) {
        Optional<Staff> staff = staffService.login(phone, password);
        if(staff.isPresent()) {
            model.addAttribute("staff", staff.get());
            // 修改这里，确保正确使用模板名称
            return staff.get().isManager() ? "manager_homepage" : "trainer_homepage";
        } else {
            model.addAttribute("error", "Invalid credentials");
            return "redirect:/login/staff?error=true";
        }
    }


}