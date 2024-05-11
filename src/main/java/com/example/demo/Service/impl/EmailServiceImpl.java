package com.example.demo.Service.impl;
import com.example.demo.Service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;


import java.util.Random;

@Service
public class EmailServiceImpl implements EmailService {
    @Autowired
    private JavaMailSender mailSender;

    @Override
    public String sendVerificationCode(String email) {
        String verificationCode = String.format("%06d", new Random().nextInt(999999));
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("1737835299@qq.com");  // 确保这里使用了和SMTP用户名相同的邮箱地址
        message.setTo(email);
        message.setSubject("Your Verification Code");
        message.setText("Your verification code is: " + verificationCode);
        mailSender.send(message);
        return verificationCode;
    }


    @Override
    public boolean verifyCode(String code, String userCode) {
        return code != null && code.equals(userCode);
    }
}