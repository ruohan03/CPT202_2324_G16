package com.example.demo.Controller;

import com.example.demo.Model.Course;
import com.example.demo.Model.Staff;
import com.example.demo.Service.CourseService;
import com.example.demo.Service.StaffService;
import com.example.demo.exception.ResourceNotFoundException;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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

    @GetMapping("/staff/login")
    public String showLoginForm() {
        return "login_staff";
    }

    @PostMapping("/staff/login")
    public String login(@RequestParam String phone, @RequestParam String password, HttpSession session, Model model) {
        Optional<Staff> staff = staffService.login(phone, password);
        if (staff.isPresent()) {
            session.setAttribute("staffId", staff.get().getStaffId());  // 设置会话属性
            model.addAttribute("staff", staff.get());
            return staff.get().isManager() ? "manager_homepage" : "trainer_homepage";
        } else {
            model.addAttribute("error", "Invalid credentials");
            return "redirect:/staff/login?error=true";
        }
    }


    @GetMapping("/staff/profile")
    public String showStaffProfile(HttpSession session, Model model) {
        String staffId = (String) session.getAttribute("staffId");
        if (staffId == null) {
            // 没有找到 staff_id 时，重定向到登录页面
            return "redirect:/staff/login";
        }

        Staff staff = staffService.findStaffById(staffId);
        if (staff != null) {
            // 如果找到员工，将其添加到模型中并跳转到员工资料页面
            model.addAttribute("staff", staff);
            model.addAttribute("currentPage", "staffProfile");
            return "staff-profile";
        } else {
            // 如果没有找到员工，重定向到首页或其他适当页面
            return "redirect:/";
        }
    }



    @PostMapping("/staff/update/{staffId}")
    public String updateStaff(@PathVariable String staffId, @ModelAttribute Staff staff, HttpSession session, RedirectAttributes redirectAttributes) {
        String sessionStaffId = (String) session.getAttribute("staffId");
        if (sessionStaffId == null|| !sessionStaffId.equals(staffId)) {
            redirectAttributes.addFlashAttribute("error", "Session expired or unauthorized access.");
            return "redirect:/staff/login";  // 确保重定向到正确的登录页
        }

        staff.setStaffId(sessionStaffId); // 设置会话中的ID到员工对象
        boolean updateResult = staffService.updateStaff(staff);
        if (updateResult) {
            redirectAttributes.addFlashAttribute("success", "Successfully updated");

        } else {
            redirectAttributes.addFlashAttribute("error", "Update failed, please try again");

        }
        return "redirect:/staff/profile";
    }

    @GetMapping("/forgetPassword_trainer")
    public String showForgetPasswordForm() {
        return "forgetPassword_trainer";
    }

    @PostMapping("/forgetPassword_trainer")
    public String resetPassword(@RequestParam String phone, @RequestParam String name, @RequestParam String newPassword, RedirectAttributes redirectAttributes) {
        Staff staff = staffService.findByPhoneAndName(phone, name);
        if (staff != null) {
            if (staff.isManager()) {
                redirectAttributes.addFlashAttribute("errorMessage", "Administrators cannot update passwords.");
            } else {
                if (!newPassword.matches("^(?=.*[0-9])(?=.*[a-zA-Z]).{5,}$")) {
                    redirectAttributes.addFlashAttribute("errorMessage", "Password must contain at least one letter and one number, and be at least 5 characters long.");
                } else {
                    staff.setPassword(newPassword);
                    staffService.updateStaff(staff);
                    redirectAttributes.addFlashAttribute("successMessage", "Password updated successfully.");
                    return "redirect:/staff/login?password_updated=true";
                }
            }
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Invalid phone or name.");
        }
        return "redirect:/forgetPassword_trainer";
    }

}


