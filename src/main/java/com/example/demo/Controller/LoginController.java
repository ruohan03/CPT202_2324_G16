package com.example.demo.Controller;

import com.example.demo.Model.Member;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import com.example.demo.Service.MemberService;  // 您的服务层

@Controller
public class LoginController {

    @Autowired
    private MemberService memberService;  // 注入服务层

    @GetMapping("/home_page")
    public String homePage() {
        return "home_page";  // 确保这里的视图名称与位于 src/main/resources/templates 目录下的 HTML 文件名匹配
    }

    @GetMapping("/login")
    public String showLoginForm() {
        return "login";  // 返回登录页面的视图名称
    }

    @PostMapping("/login")
    public ModelAndView login(
            @RequestParam("phone") String phone,
            @RequestParam("password") String password,
            @RequestParam("loginType") String loginType,
            HttpSession session) {

        Member member = memberService.validateMember(phone, password);

        if (member != null) {
            session.setAttribute("currentUser", member);
            boolean isManagerLogin = "admin".equals(loginType);

            if (member.isManager() && !isManagerLogin) {
                return new ModelAndView("redirect:/login?error=admin");  // 提示管理员去管理员界面登录
            } else if (!member.isManager() && isManagerLogin) {
                return new ModelAndView("redirect:/login?error=user");  // 提示用户去用户界面登录
            } else if (member.isManager()) {
                return new ModelAndView("redirect:/members");  // 管理员主页
            } else {
                return new ModelAndView("redirect:/home_page");  // 用户主页
            }
        } else {
            ModelAndView modelAndView = new ModelAndView("login");
            modelAndView.addObject("error", "Invalid phone number or password.");  // 登录失败返回登录页面并显示错误
            return modelAndView;
        }
    }
}

