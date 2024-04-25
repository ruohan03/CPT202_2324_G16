package com.example.demo.exception;

public class MembershipValidationException extends RuntimeException {
    public MembershipValidationException(String message) {
        super(message);
    }
}
