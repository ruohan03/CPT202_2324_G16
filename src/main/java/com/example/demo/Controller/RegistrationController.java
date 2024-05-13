package com.example.demo.Controller;
// Controller层的代码示例

import com.example.demo.Model.Member;
import com.example.demo.Service.EmailService;
import com.example.demo.Service.MemberService;
import com.example.demo.Service.PasswordService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class RegistrationController {

    @Autowired
    private MemberService memberService;

    @Autowired
    private PasswordService passwordService;

    @Autowired
    private EmailService emailService;

    @GetMapping("/register")
    public ModelAndView registrationForm() {
        ModelAndView mav = new ModelAndView("register");
        mav.addObject("member", new Member());
        return mav;
    }


    @PostMapping("/register")
    public ModelAndView registerMember(Member member, String verificationCode, HttpServletRequest request) {
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
        member.setEmail(request.getParameter("email"));

        // 检查验证码和时间有效性
        String sessionCode = (String) request.getSession().getAttribute("VERIFICATION_CODE");
        Long codeTimestamp = (Long) request.getSession().getAttribute("CODE_TIMESTAMP");
        long currentTime = System.currentTimeMillis();

        if (codeTimestamp == null || currentTime > codeTimestamp + 5 * 60 * 1000) {
            mav.addObject("error", "Verification code has expired.");
            mav.setViewName("register");
            return mav;
        }

        if (!emailService.verifyCode(sessionCode, verificationCode)) {
            mav.addObject("error", "Invalid verification code.");
            mav.setViewName("register");
            return mav;
        }

        String encodedPassword = passwordService.encodePassword(member.getPassword());
        member.setEncodedPassword(encodedPassword);
        memberService.saveMember(member);
        request.getSession().removeAttribute("VERIFICATION_CODE");  // 清除session中的验证码
        request.getSession().removeAttribute("CODE_TIMESTAMP");  // 清除时间戳

        mav.setViewName("redirect:/login");
        return mav;
    }

    @PostMapping("/sendVerificationCode")
    public ResponseEntity<?> sendVerificationCode(@RequestParam String email, HttpServletRequest request) {
        Long lastSent = (Long) request.getSession().getAttribute("LAST_SENT_TIMESTAMP");
        long now = System.currentTimeMillis();

        // 检查上次发送时间是否在5分钟以内
        if (lastSent != null && (now - lastSent) < 5 * 60 * 1000) {
            return ResponseEntity.badRequest().body("Please wait before requesting another verification code.");
        }

        try {
            String code = emailService.sendVerificationCode(email);
            request.getSession().setAttribute("VERIFICATION_CODE", code);  // 保存验证码到session
            request.getSession().setAttribute("CODE_TIMESTAMP", now);  // 保存验证码生成的时间戳
            request.getSession().setAttribute("LAST_SENT_TIMESTAMP", now);  // 更新最后发送时间
            return ResponseEntity.ok("Verification code sent successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to send verification code.");
        }
    }


}
