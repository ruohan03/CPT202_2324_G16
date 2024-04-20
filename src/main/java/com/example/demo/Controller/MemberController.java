package com.example.demo.Controller;

import com.example.demo.Model.Member;
import com.example.demo.Service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

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

    @GetMapping("/home_page")
    public String homePage(Model model) {
        // 添加逻辑，如向模型添加数据
        return "home_page";  // 这里是返回视图名称
    }

    @PostMapping("/login")
    public String login(@RequestParam("phone") String phone, @RequestParam("password") String password, Model model) {
        Optional<Member> memberOpt = memberService.login(phone, password);
        if (memberOpt.isPresent()) {
            Member member = memberOpt.get();
            model.addAttribute("member", member);
            // 修改这里，确保正确使用模板名称
            return "redirect:/home_page"; // 如果验证成功，重定向到主页
        } else {
            model.addAttribute("error", "Invalid credentials");
            return "redirect:/login?error=true"; // 如果验证失败，重定向回登录页面并显示错误信息
        }
    }
}