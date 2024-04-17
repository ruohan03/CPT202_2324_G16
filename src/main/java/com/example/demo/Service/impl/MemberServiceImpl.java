package com.example.demo.Service.impl;

import com.example.demo.Model.Member;
import com.example.demo.Service.MemberService;
import com.example.demo.repo.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MemberServiceImpl implements MemberService {
    @Autowired
    private MemberRepository memberRepository;

    @Override
    public void demoMethod() {
        System.out.println("This is a demo method for Member");
    }

    @Override
    public void memberMethod() {}

    @Override
    public List<Member> findAllMembers() {
        return memberRepository.findAll();
    }

    @Override
    public void addMember(Member member) {
        memberRepository.save(member);
    }

    @Override
    public boolean validateMember(String phoneNumber, String password) {
        Member member = memberRepository.findByPhone(phoneNumber);
        return member != null && member.getPassword().equals(password);
    }
}
