package com.example.demo.Controller;

import com.example.demo.Model.Member;
import com.example.demo.Service.EmailService;
import com.example.demo.Service.MemberService;
import com.example.demo.Service.PasswordService;
import com.example.demo.Service.PurchaseService;
import com.example.demo.exception.MembershipValidationException;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.example.demo.util.Constants.*;

@Controller
public class MemberController {
    private final MemberService memberService;

    @Autowired
    private PasswordService passwordService;

    @Autowired
    private EmailService emailService;
    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    private static final Logger log = LoggerFactory.getLogger(MemberController.class);

    private boolean validateMember(String memberId, Model model) {
        Member member = memberService.findMemberById(memberId);
        if (member == null) {
            model.addAttribute("errorMessage", "Member not found");
            return false;
        }
        model.addAttribute("member", member);
        return true;
    }


    @GetMapping("/members")
    public String getAllMembers(Model model) {
        List<Member> members = memberService.findAllMembers();
        log.info("Members: {}", members);
        model.addAttribute("members", members);
        return "Member";
    }

    @PostMapping("/addMember")
    public String addMember(@ModelAttribute("member") Member member) {
        memberService.addMember(member);
        return "redirect:/members"; // 重定向到显示所有会员的页面
    }

    @GetMapping("/member/details/{memberId}")
    public String showMemberDetails(@PathVariable("memberId") String memberId, Model model) {
        Member member = memberService.findMemberById(memberId);
        if (validateMember(memberId, model)) {
            model.addAttribute("member", member);
            return "member-detail";  // 确保模板名称正确
        } else {
            model.addAttribute("errorMessage", "Member not found");
            return "redirect:/members";  // 或处理错误情况
        }
    }

    @GetMapping("/login")
    public String showLoginForm() {
        return "login";  // 返回登录页面的视图名称
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate(); // 销毁session
        return "redirect:/login"; // 重定向到登录页面
    }

    // 显示主页或其他需要会员ID的方法
    @GetMapping("/home_page")
    public String homePage(Model model, HttpSession session) {
        String memberId = (String) session.getAttribute("memberId");
        Member member = null;
        if (memberId != null) {
            member = memberService.findMemberById(memberId);
        }

        if (member != null) {
            model.addAttribute("member", member);
            model.addAttribute("memberId", memberId);
            model.addAttribute("isLoggedIn", true);
            model.addAttribute("welcomeMessage", "Welcome back, " + member.getName() + "!");

            // 检查续费通知是否已发送，并传递续费通知
            if (member.isRenewalNotificationSent()) {
                model.addAttribute("renewalMessage", "Your membership is expiring soon. Please consider renewing to continue enjoying our services.");
            }
        } else {
            model.addAttribute("isLoggedIn", false);
            model.addAttribute("welcomeMessage", "Welcome to the Fitness Center!");
        }
        model.addAttribute("currentPage", "home");
        return "home_page";
    }



    @PostMapping("/login")
    public String login(@RequestParam("phone") String phone, @RequestParam("password") String password, Model model, HttpSession session, HttpServletRequest request) {
        Optional<Member> memberOpt = memberService.login(phone, password);
        if (memberOpt.isPresent()) {
            session.invalidate();
            HttpSession newSession = request.getSession(true);
            newSession.setAttribute("memberId", memberOpt.get().getMemberId());
            return "redirect:/home_page"; // 如果验证成功，重定向到主页
        } else {
            model.addAttribute("error", "Invalid credentials");
            return "redirect:/login?error=true"; // 如果验证失败，重定向回登录页面并显示错误信息
        }
    }


    @GetMapping("/member/profile")
    public String showProfile(HttpSession session, Model model) {
        String memberId = (String) session.getAttribute("memberId");
        if (!validateMember(memberId, model)) {
            return "redirect:/login";
        }
        Member member = memberService.findMemberById(memberId);
        if (validateMember(memberId, model)) {
            model.addAttribute("member", member);
            model.addAttribute("isLoggedIn", true);
            model.addAttribute("currentPage", "memberProfile"); // 添加当前页面的标识
            return "member-profile";
        } else {
            return "redirect:/";
        }
    }

    @PostMapping("/member/update/{memberId}")
    public String updateMember(@PathVariable String memberId, @ModelAttribute Member member, HttpSession session, RedirectAttributes redirectAttributes) {
        String sessionMemberId = (String) session.getAttribute("memberId");
        if (sessionMemberId == null || !sessionMemberId.equals(memberId)) {
            redirectAttributes.addFlashAttribute("error", "Unauthorized access or session has expired");
            return "redirect:/login";
        }
        boolean updateResult = memberService.updateMember(member);
        if (updateResult) {
            redirectAttributes.addFlashAttribute(SUCCESS, "Successfully updated");
        } else {
            redirectAttributes.addFlashAttribute(ERROR, "Update failed, please try again");
        }
        return "redirect:/member/profile";
    }

    @GetMapping("/member/update-password")
    public String showChangePasswordForm(HttpSession session, Model model) {
        String memberId = (String) session.getAttribute("memberId");
        if (!validateMember(memberId, model)) {
            return "redirect:/login";
        }
        Member member = memberService.findMemberById(memberId);
        if (validateMember(memberId, model)) {
            model.addAttribute("member", member);
            model.addAttribute("isLoggedIn", true);
            model.addAttribute("currentPage", "updatePassword");
            return "update-password";
        } else {
            return "redirect:/";
        }
    }

    @PostMapping("/member/update-password")
    public String updatePassword(@RequestParam String oldPassword, @RequestParam String newPassword,
                                 @RequestParam String confirmPassword, HttpSession session, RedirectAttributes redirectAttributes) {
        String memberId = (String) session.getAttribute("memberId");
        if (memberId == null) {
            redirectAttributes.addFlashAttribute(ERROR, SESSION_EXPIRED);
            return "redirect:/login";
        }
        if (!newPassword.equals(confirmPassword)) {
            redirectAttributes.addFlashAttribute(ERROR, PASSWORD_MISMATCH);
            return "redirect:/member/update-password";
        }
        String updateResult = memberService.changePassword(memberId, oldPassword, newPassword, redirectAttributes);
        if (updateResult.equals("redirect:/home_page")) {
            redirectAttributes.addFlashAttribute(SUCCESS, PASSWORD_CHANGE_SUCCESS);
        }
        return updateResult;
    }

    @GetMapping("/members/check-validity/{memberId}")
    @ResponseBody
    public ResponseEntity<?> checkMembershipValidity(@PathVariable String memberId) {
        try {
            memberService.checkMembershipStatus(memberId);
            return ResponseEntity.ok().build();  // 会员状态有效
        } catch (MembershipValidationException e) {
            return ResponseEntity.badRequest().body(e.getMessage());  // 返回错误信息
        }
    }

    @GetMapping("/forgot-password")
    public String showForgotPasswordForm() {
        return "forgot-password"; // 返回忘记密码页面的视图名称
    }

    @PostMapping("/sendForgotPasswordCode")
    public ResponseEntity<?> sendForgotPasswordCode(@RequestParam String email, @RequestParam String phone, HttpServletRequest request) {
        Member member = memberService.findMemberByPhone(phone);
        if (member == null || !member.getEmail().equals(email)) {
            return ResponseEntity.badRequest().body("{\"message\":\"The email does not match the one registered with this phone number.\"}");
        }

        Long lastSent = (Long) request.getSession().getAttribute("LAST_SENT_TIMESTAMP");
        long now = System.currentTimeMillis();

        if (lastSent != null && (now - lastSent) < 5 * 60 * 1000) {
            return ResponseEntity.badRequest().body("{\"message\":\"Please wait before requesting another verification code.\"}");
        }

        try {
            String code = emailService.sendVerificationCode(email);
            request.getSession().setAttribute("VERIFICATION_CODE", code);
            request.getSession().setAttribute("CODE_TIMESTAMP", now);
            request.getSession().setAttribute("LAST_SENT_TIMESTAMP", now);
            return ResponseEntity.ok("{\"message\":\"Verification code sent successfully.\"}");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("{\"message\":\"Failed to send verification code.\"}");
        }
    }



    @PostMapping("/forgot-password")
    public String processForgotPassword(@RequestParam("phone") String phone, @RequestParam("username") String username,
                                        @RequestParam("email") String email, @RequestParam("newPassword") String newPassword,
                                        @RequestParam("verificationCode") String verificationCode, HttpServletRequest request,
                                        RedirectAttributes redirectAttributes) {
        Member member = memberService.findByPhoneAndName(phone, username);
        if (member != null && member.getEmail().equals(email)) {
            String sessionCode = (String) request.getSession().getAttribute("VERIFICATION_CODE");
            Long codeTimestamp = (Long) request.getSession().getAttribute("CODE_TIMESTAMP");
            long currentTime = System.currentTimeMillis();

            // 验证码有效性检查
            if (codeTimestamp == null || currentTime > codeTimestamp + 5 * 60 * 1000) {
                redirectAttributes.addFlashAttribute("errorMessage", "Verification code has expired.");
                return "redirect:/forgot-password";
            }

            if (!emailService.verifyCode(sessionCode, verificationCode)) {
                redirectAttributes.addFlashAttribute("errorMessage", "Invalid verification code.");
                return "redirect:/forgot-password";
            }

            // 密码复杂性检查
            if (!newPassword.matches("^(?=.*[a-zA-Z])(?=.*\\d).{5,}$")) {
                redirectAttributes.addFlashAttribute("errorMessage", "Password must contain both letters and digits and the minimum length is 5.");
                return "redirect:/forgot-password";
            }

            boolean updateResult = memberService.updatePassword(phone, newPassword);
            if (updateResult) {
                redirectAttributes.addFlashAttribute("successMessage", "Password updated successfully.");
                String encodedPassword = passwordService.encodePassword(newPassword);
                member.setEncodedPassword(encodedPassword);
                memberService.updateMember(member);
                request.getSession().removeAttribute("VERIFICATION_CODE");  // 清除session中的验证码
                request.getSession().removeAttribute("CODE_TIMESTAMP");  // 清除时间戳
                return "redirect:/login?password_updated=true";
            } else {
                redirectAttributes.addFlashAttribute("errorMessage", "Failed to update password, please try again later.");
                return "redirect:/forgot-password";
            }
        } else {
            // 用户不存在或电话和用户名不匹配，显示错误消息
            redirectAttributes.addFlashAttribute("errorMessage", "Incorrect username, phone number, or email.");
            return "redirect:/forgot-password";
        }
    }



}


