package com.example.demo.Controller;

import com.example.demo.Model.Member;
import com.example.demo.Service.MemberService;
import com.example.demo.util.Constants;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

import static com.example.demo.util.Constants.*;

@Controller
public class MemberController {
    @Autowired
    private MemberService memberService;

    @GetMapping("/members")
    public String getAllMembers(Model model){
        List<Member> members = memberService.findAllMembers();
        System.out.println(members); // 用于调试，查看获取的会员信息
        model.addAttribute("members", members);
        return "Member";
    }

    @PostMapping("/addMember")
    public String addMember(@ModelAttribute("member") Member member){
        memberService.addMember(member);
        return "redirect:/members"; // 重定向到显示所有会员的页面
    }

    @GetMapping("/member/details/{member_id}")
    public String showMemberDetails(@PathVariable("member_id") String memberId, Model model) {
        Member member = memberService.findMemberById(memberId);
        if (member != null) {
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
        String memberId = (String) session.getAttribute("member_id");
        if (memberId != null) {
            Member member = memberService.findMemberById(memberId);
            if (member != null) {
                model.addAttribute("member", member);
                model.addAttribute("isLoggedIn", true);
                model.addAttribute("welcomeMessage", "Welcome back, " + member.getName() + "!");
                model.addAttribute("currentPage", "home"); // 添加当前页面的标识
            } else {
                model.addAttribute(ERROR, MEMBER_NOT_FOUND);
                return "redirect:/login";
            }
        } else {
            model.addAttribute("isLoggedIn", false);
        }
        return "home_page";
    }

    @PostMapping("/login")
    public String login(@RequestParam("phone") String phone, @RequestParam("password") String password, Model model, HttpSession session) {
        Optional<Member> memberOpt = memberService.login(phone, password);
        if (memberOpt.isPresent()) {
            Member member = memberOpt.get();
            session.setAttribute("member_id", member.getMember_id()); // 将会员ID存储到session中
            model.addAttribute("member", member);
            return "redirect:/home_page"; // 如果验证成功，重定向到主页
        } else {
            model.addAttribute("error", "Invalid credentials");
            return "redirect:/login?error=true"; // 如果验证失败，重定向回登录页面并显示错误信息
        }
    }

    @GetMapping("/member/profile")
    public String showProfile(HttpSession session, Model model) {
        String memberId = (String) session.getAttribute("member_id");
        if (memberId == null) {
            return "redirect:/login";
        }
        Member member = memberService.findMemberById(memberId);
        if (member != null) {
            model.addAttribute("member", member);
            model.addAttribute("currentPage", "memberProfile"); // 添加当前页面的标识
            return "member-profile";
        } else {
            return "redirect:/";
        }
    }

    @PostMapping("/member/update/{member_id}")
    public String updateMember(@PathVariable String member_id, @ModelAttribute Member member, HttpSession session, RedirectAttributes redirectAttributes) {
        String sessionMemberId = (String) session.getAttribute("member_id");
        if (sessionMemberId == null || !sessionMemberId.equals(member_id)) {
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
        String memberId = (String) session.getAttribute("member_id");
        if (memberId == null) {
            return "redirect:/login";
        }
        Member member = memberService.findMemberById(memberId);
        if (member != null) {
            model.addAttribute("member", member);
            model.addAttribute("currentPage","updatePassword");
            return "update-password";
        }else {
            return "redirect:/";
        }
    }

    @PostMapping("/member/update-password")
    public String updatePassword(@RequestParam String oldPassword, @RequestParam String newPassword,
                                 @RequestParam String confirmPassword, HttpSession session, RedirectAttributes redirectAttributes) {
        String memberId = (String) session.getAttribute("member_id");
        if (memberId == null) {
            redirectAttributes.addFlashAttribute(ERROR, SESSION_EXPIRED);
            return "redirect:/login";
        }
//        if (!memberService.isPasswordComplex(newPassword)) {
//            redirectAttributes.addFlashAttribute(ERROR, WEAK_PASSWORD);
//            return "redirect:/member/update-password";
//        }
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

}