package com.example.demo.Service;

import com.example.demo.Service.impl.PasswordServiceImpl;

public interface PasswordService {
    String encodePassword(String password);
    boolean matchPassword(String rawPassword, String encodedPassword);
}