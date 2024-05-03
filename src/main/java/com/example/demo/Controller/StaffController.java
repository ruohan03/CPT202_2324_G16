package com.example.demo.Controller;

import com.example.demo.Model.Course;
import com.example.demo.Model.Member;
import com.example.demo.Model.Staff;
import com.example.demo.Service.CourseService;
import com.example.demo.Service.MemberService;
import com.example.demo.Service.PasswordService;
import com.example.demo.Service.StaffService;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repo.StaffRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.*;
import java.util.stream.Collectors;

@Controller
public class StaffController {
    @Autowired
    private StaffService staffService;
    @Autowired
    private CourseService courseService;

    @Autowired
    private PasswordService passwordService;

    @Autowired
    private StaffRepository staffRepository;
    @Autowired
    private MemberService memberService;

    @GetMapping("/trainer/homepage")
    public String trainerHomepage(){
        return "trainer_homepage";
    }

    @GetMapping("/admin/homepage")
    public String managerHomepage(){
        return "manager_homepage";
    }



    @GetMapping("/staff")
    public String getAllStaff(Model model){
        List<Staff> staff = staffService.findAllStaff();
        System.out.println(staff); // 用于调试，查看获取的员工信息
        model.addAttribute("staff", staff);
        return "staffView";  // 指向你的员工视图的Thymeleaf模板
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
            session.setAttribute("isManager", staff.get().isManager());  // 设置是否为管理员的标识
            model.addAttribute("staff", staff.get());
            return staff.get().isManager() ? "redirect:/admin/homepage" : "redirect:/trainer/homepage";
        } else {
            // We need to check if the staff exists but is not active to provide a specific error message
            Staff inactiveStaff = staffRepository.findByPhone(phone);
            if (inactiveStaff != null && !inactiveStaff.isActive()) {
                model.addAttribute("error", "Account not active. Please contact administrator.");
                return "redirect:/staff/login?error=inactive";
            } else {
                model.addAttribute("error", "Invalid credentials");
                return "redirect:/staff/login?error=true";
            }
        }
    }




    @GetMapping("/staff/profile")
    public String showStaffProfile(HttpSession session, Model model) {
        String staffId = (String) session.getAttribute("staffId");
        if (staffId == null) {
            // 没有找到 staffId 时，重定向到登录页面
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

                    String encodedPassword = passwordService.encodePassword(staff.getPassword());
                    staff.setEncodedPassword(encodedPassword);
                    // 保存会员信息到数据库
                    staffService.updateStaff(staff);

                    return "redirect:/staff/login?password_updated=true";
                }
            }
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Invalid phone or name.");
        }
        return "redirect:/forgetPassword_trainer";
    }


    @GetMapping("/admin/renewal-notifications")
    public String showRenewalNotificationPage(HttpSession session, Model model) {
        Boolean isManager = (Boolean) session.getAttribute("isManager");
        if (Boolean.TRUE.equals(isManager)) {
            // 获取即将到期的会员列表
            List<Member> expiringMembers = staffService.findMembersForRenewalNotification(Calendar.getInstance().getTime());
            // 获取已发送通知的会员列表
            List<Member> notifiedMembers = staffService.findMembersWithSentNotifications();

            model.addAttribute("expiringMembers", expiringMembers);
            model.addAttribute("notifiedMembers", notifiedMembers);
            return "adminRenewalNotifications"; // Thymeleaf页面显示即将到期和已通知的会员列表
        } else {
            return "redirect:/staff/login";
        }
    }


    /**
     * 管理员发送续费通知
     * 只有管理员可以执行此操作
     */
    @PostMapping("/admin/send-renewal-notifications")
    public String sendRenewalNotifications(HttpSession session, RedirectAttributes redirectAttributes) {
        Boolean isManager = (Boolean) session.getAttribute("isManager");
        if (Boolean.TRUE.equals(isManager)) {
            staffService.sendRenewalNotifications();
            redirectAttributes.addFlashAttribute("success", "Renewal notifications sent successfully.");
            return "redirect:/admin/renewal-notifications";
        } else {
            redirectAttributes.addFlashAttribute("error", "Unauthorized access.");
            return "redirect:/staff/login";
        }
    }

    @GetMapping("/manage_staff")
    public String manageStaffList(@RequestParam(name = "search", required = false) String search, Model model, RedirectAttributes redirectAttributes) {
        List<Staff> staff;
        Set<Staff> results = new HashSet<>();

        if (search != null && !search.isEmpty()) {
            // 根据name、staff_id、gender、course_id进行搜索
            results.addAll(staffService.findStaffByNameContaining(search).stream()
                    .filter(s -> !s.isManager())
                    .collect(Collectors.toSet()));
            results.addAll(staffService.findStaffByStaffIdContaining(search).stream()
                    .filter(s -> !s.isManager())
                    .collect(Collectors.toSet()));
            results.addAll(staffRepository.findByGender(search).stream()
                    .filter(s -> !s.isManager())
                    .collect(Collectors.toSet()));
            results.addAll(staffService.findStaffByCourseIdContaining(search).stream()
                    .filter(s -> !s.isManager())
                    .collect(Collectors.toSet()));

            // 尝试将search转换为整数，以便根据level进行搜索
            try {
                int level = Integer.parseInt(search);
                results.addAll(staffRepository.findByLevel(level).stream()
                        .filter(s -> !s.isManager())
                        .collect(Collectors.toSet()));
            } catch (NumberFormatException e) {
                // 如果search不能转换为整数，这里可以不处理，继续其他搜索
            }

            // 如果results为空，重定向并显示消息
            if (results.isEmpty()) {
                redirectAttributes.addFlashAttribute("noSearchMessage", "No Trainer found for your search");
                return "redirect:/manage_staff";
            }
        } else {
            // 如果没有搜索条件，显示所有员工
            results.addAll(staffService.findAllStaff().stream()
                    .filter(s -> !s.isManager())
                    .collect(Collectors.toSet()));
        }

        staff = new ArrayList<>(results);
        model.addAttribute("staff", staff);
        return "manage_staff";
    }

    @GetMapping ("/delete_staff/{id}")
    public ResponseEntity<String> deleteStaff(@PathVariable(value="id") String staffId) {
        if (!staffService.existsByStaffId(staffId)) {
            return ResponseEntity.badRequest().body("<script>alert('Staff with ID " + staffId + " not found'); window.location.href='/manage_staff';</script>");
        }
        String confirmationMessage = "Are you sure you want to delete the staff whose staff ID is "+ staffId +" ? Once deleted, it cannot be withdrawn.";
        // 生成包含确认消息的JavaScript代码
        String confirmScript = "if(confirm('" + confirmationMessage + "')) { "
                + "window.location.href='/delete_staff_confirmed/" + staffId + "';"
                + "} else {"
                + "window.location.href='/manage_staff';"
                + "}";
        // 返回包含确认消息的 JavaScript 响应
        return ResponseEntity.ok("<script>" + confirmScript + "</script>");
    }

    @GetMapping("/delete_staff_confirmed/{id}")
    public ResponseEntity<String> deleteStaffConfirmed(@PathVariable(value="id") String staffId) {
        if (!staffService.existsByStaffId(staffId)) {
            return ResponseEntity.badRequest().body("<script>alert('Staff with ID " + staffId + " not found'); window.location.href='/manage_staff';</script>");
        }
        // 执行删除操作
        staffService.deleteByStaffId(staffId);
        // 删除成功后的消息
        String successMessage = "Staff with ID " + staffId + " has been successfully deleted.";
        return ResponseEntity.ok("<script>alert('" + successMessage + "'); window.location.href='/manage_staff';</script>");
    }

    @GetMapping("/edit_staff/{staffId}")
    public String showEditCourseForm(@PathVariable("staffId") String staffId, Model model) {
        Staff staff = staffService.findStaffById(staffId);
        if (staff != null) {
            model.addAttribute("staff", staff);
            return "edit_staff";
        } else {
            // Staff not found, redirect or show error message
            return "redirect:/manage_staff";
        }
    }

    @PostMapping("/edit_staff/{staffId}")
    public ResponseEntity<String> editStaff(@PathVariable("staffId") String staffId, @ModelAttribute Staff updatedStaff, @RequestParam(defaultValue = "false") boolean isActive) {
        // 验证courseId是否有效
        if (!courseService.existsByCourseId(updatedStaff.getCourseId())) {
            return ResponseEntity.badRequest().body("<script>alert('Invalid Course ID " + updatedStaff.getCourseId() + "'); window.location.href='/manage_staff';</script>");
        }

        // 设置isActive状态
        updatedStaff.setActive(isActive);

        // 确保使用正确的staffId
        updatedStaff.setStaffId(staffId);

        // 调用服务层更新员工信息
        boolean updateSuccess = staffService.updateStaff(updatedStaff);
        if (updateSuccess) {
            return ResponseEntity.ok("<script>alert('Staff updated successfully'); window.location.href='/manage_staff';</script>");
        } else {
            return ResponseEntity.badRequest().body("<script>alert('Error updating staff'); window.location.href='/manage_staff';</script>");
        }
    }



    @GetMapping("/add_staff")
    public String addNewStaffPage() {
        return "add_staff";
    }
    @PostMapping("/add_staff")
    public ResponseEntity<String> addStaff(@ModelAttribute Staff staff) {

        if (!staff.getStaffId().matches("^T\\d{3}$")) {
            return ResponseEntity.badRequest().body("<script>alert('Staff ID must be Txxx (x is number)'); window.location.href='/manage_staff';</script>");
        }

        if (staffService.existsByStaffId(staff.getStaffId())) {
            return ResponseEntity.badRequest().body("<script>alert('Staff with ID " + staff.getStaffId() + " already exists'); window.location.href='/manage_staff';</script>");
        }

        if (!courseService.existsByCourseId(staff.getCourseId())) {
            return ResponseEntity.badRequest().body("<script>alert('Invalid Course ID " + staff.getCourseId() + "'); window.location.href='/manage_staff';</script>");
        }

        staffService.addStaff(staff);
        String encodedPassword = passwordService.encodePassword(staff.getPassword());
        staff.setEncodedPassword(encodedPassword);
        // 保存会员信息到数据库
        staffService.saveOrUpdate(staff);

        return ResponseEntity.ok("<script>alert('Staff added successfully'); window.location.href='/manage_staff';</script>");
    }

    @GetMapping("/manage-members")
    public String manageMembersList(@RequestParam(name = "search", required = false) String search, Model model, RedirectAttributes redirectAttributes) {
        List<Member> members;
        Set<Member> results = new HashSet<>();

        if (search != null && !search.isEmpty()) {
            // 根据name、memberId、phone和gender进行搜索
            results.addAll(memberService.findMemberByPhoneContaining(search).stream()
                    .collect(Collectors.toSet()));
            // 如果results为空，重定向并显示消息
            if (results.isEmpty()) {
                redirectAttributes.addFlashAttribute("noSearchMessage", "No member phone,please check and try again!");
                return "redirect:/manage-members";
            }
        } else {
            // 如果没有搜索条件，显示所有会员
            results.addAll(memberService.findAllMembers().stream()
                    .collect(Collectors.toSet()));
        }

        members = new ArrayList<>(results);
        model.addAttribute("members", members);
        return "adminManageMembers"; // Thymeleaf页面，用于显示所有会员列表和搜索功能
    }



    @PostMapping("/recharge-member")
    public String rechargeMember(HttpSession session, @RequestParam String memberId, @RequestParam double amount, RedirectAttributes redirectAttributes) {
        Boolean isManager = (Boolean) session.getAttribute("isManager");
        if (Boolean.TRUE.equals(isManager)) {
            try {
                memberService.rechargeMember(memberId, amount);
                redirectAttributes.addFlashAttribute("success", "Recharge successful for member ID: " + memberId);
            } catch (Exception e) {
                redirectAttributes.addFlashAttribute("error", "Recharge failed: " + e.getMessage());
            }
            return "redirect:/manage-members";
        } else {
            redirectAttributes.addFlashAttribute("error", "Unauthorized access.");
            return "redirect:/staff/login";
        }
    }

}





