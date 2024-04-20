package com.example.demo.Controller;

import com.example.demo.Model.Member;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import com.example.demo.Service.MemberService;

@Controller
public class LoginController {

    @Autowired
    private MemberService memberService;

    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }

    @PostMapping("/login")
    public ModelAndView login(
            @RequestParam("phone") String phone,
            @RequestParam("password") String password,
            @RequestParam("loginType") String loginType,
           HttpSession session) {
//改！！！！！！！！！！！！！！
        Member member = memberService.validateMember(phone, password);
        if (member != null && ((member.isManager() && "admin".equals(loginType)) || (!member.isManager() && "user".equals(loginType)))) {
            session.setAttribute("currentUser", member);
            return new ModelAndView("redirect:/home_page");
        } else {
            return new ModelAndView("login").addObject("error", "Your login information is incorrect.");
        }
    }
}
