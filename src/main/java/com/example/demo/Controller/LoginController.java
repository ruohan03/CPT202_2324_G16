package com.example.demo.Controller;

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
            HttpSession session) {

        boolean isValidUser = memberService.validateMember(phone, password);

        if (isValidUser) {
            session.setAttribute("currentUser", phone);  // 将电话号码存储在会话中标识登录状态
            return new ModelAndView("redirect:/home_page");  // 登录成功跳转
        } else {
            ModelAndView modelAndView = new ModelAndView("login");
            modelAndView.addObject("error", "Invalid phone number or password.");  // 登录失败返回登录页面并显示错误
            return modelAndView;
        }
    }
}

