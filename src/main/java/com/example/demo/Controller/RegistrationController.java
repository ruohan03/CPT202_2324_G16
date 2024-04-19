package com.example.demo.Controller;
// Controller层的代码示例

import com.example.demo.Model.Member;
import com.example.demo.Service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class RegistrationController {

    @Autowired
    private MemberService memberService;

    @GetMapping("/register")
    public ModelAndView registrationForm() {
        ModelAndView mav = new ModelAndView("register");
        mav.addObject("member", new Member());
        return mav;
    }

    @PostMapping("/register")
    public ModelAndView registerMember(Member member) {
        ModelAndView mav = new ModelAndView();

        // 检查密码复杂性
        if (!member.getPassword().matches("^(?=.*[0-9])(?=.*[a-zA-Z]).{5,}$")) {
            mav.addObject("error", "Password must be at least 5 characters long and include both letters and numbers.");
            mav.setViewName("register");
            return mav;
        }

        // 检查手机号唯一性
        if (MemberService.findByPhone(member.getPhone()) != null) {
            mav.addObject("error", "This phone number is already registered.");
            mav.setViewName("register");
            return mav;
        }

        // 保存会员信息到数据库
        memberService.saveMember(member);

        // 注册成功后跳转到登录页或其他页面
        mav.setViewName("redirect:/login");
        return mav;
    }
}
