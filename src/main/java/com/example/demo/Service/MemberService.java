package com.example.demo.Service;

import com.example.demo.Model.Member;

import java.util.List;

public interface MemberService {
    void demoMethod();

    void memberMethod();

    List<Member> findAllMembers();

    void addMember(Member member);
}