package com.example.demo.Service;

public interface EmailService {
    String sendVerificationCode(String email);
    boolean verifyCode(String code, String userCode);
}
