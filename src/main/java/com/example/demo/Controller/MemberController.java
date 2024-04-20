package com.example.demo.Controller;

import com.example.demo.Model.Member;
import com.example.demo.Service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
}