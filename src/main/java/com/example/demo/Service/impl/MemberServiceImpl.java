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
    public Member validateMember(String phoneNumber, String password) {
        Member member = memberRepository.findByPhone(phoneNumber);
        if (member != null && member.getPassword().equals(password)) {
            return member;  // 返回 Member 对象，如果电话号码和密码匹配
        }
        return null;  // 如果密码不匹配或用户不存在，则返回 null
    }

    public Member saveMember(Member member) {
        // 当创建新成员时，设置自定义的 member_id
        if (member.getPhone() != null && !member.getPhone().isEmpty()) {
            member.setMember_id("M" + member.getPhone());
        }
        // Save the member to the database
        return memberRepository.save(member);
    }

    public Member findByPhone(String phone) {
        return memberRepository.findByPhone(phone);
    }

    @Override
    public Member findMemberById(String memberId) {
        return memberRepository.findById(memberId).orElse(null);
    }

    @Override
    public void addMember(Member member) {
        // 生成自定义的 member_id
        if (member.getPhone() != null && !member.getPhone().isEmpty()) {
            member.setMember_id("M" + member.getPhone());
        }
        memberRepository.save(member);
    }

    public String getRole(String phone) {
        Member member = memberRepository.findByPhone(phone);
        if (member != null && member.isManager()) {
            return "ADMIN";
        } else {
            return "USER";
        }
    }

}
