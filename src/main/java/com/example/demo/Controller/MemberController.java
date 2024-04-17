package com.example.demo.Controller;

import com.example.demo.Model.Member;
import com.example.demo.Service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

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
}