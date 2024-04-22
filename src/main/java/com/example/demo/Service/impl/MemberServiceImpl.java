package com.example.demo.Service.impl;

import com.example.demo.Model.Member;
import com.example.demo.Service.MemberService;
import com.example.demo.repo.MemberRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

import static com.example.demo.util.Constants.*;

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
    public Optional<Member> login(String phoneNumber, String password) {
        Member member = memberRepository.findByPhone(phoneNumber);
        if (member != null && member.getPassword() != null && member.getPassword().equals(password)) {
            return Optional.of(member); // 如果电话号码和密码匹配，则返回Optional包含Member对象
        }
        return Optional.empty(); // 如果密码不匹配或用户不存在，则返回一个空的Optional
    }

    @Transactional
    @Override
    public void saveMember(Member member) {
        // 检查电话号码是否已提供，并尝试生成基于电话号码的 member_id
        if (member.getPhone() != null && !member.getPhone().isEmpty()) {
            String proposedId = "M" + member.getPhone();
            // 检查是否已存在相同的 member_id
            if (memberRepository.existsById(proposedId)) {
                // 如果已存在，处理冲突（例如抛出异常或生成一个不同的 ID）
                throw new IllegalStateException("Member ID already exists for this phone number.");
            }
            member.setMember_id(proposedId);
        } else {
            // 如果电话号码没有提供，可以选择抛出异常或采取其他措施
            throw new IllegalArgumentException("Phone number is required to generate member ID.");
        }

        // 保存会员信息到数据库
        memberRepository.save(member);
    }

    public Member findByPhone(String phone) {
        return memberRepository.findByPhone(phone);
    }

    @Override
    public Member findMemberById(String memberId) {
        return memberRepository.findById(memberId).orElse(null);
    }

    @Transactional
    @Override
    public boolean updateMember(Member member) {
        return memberRepository.findById(member.getMember_id())
                .map(existingMember -> {
                    existingMember.setName(member.getName());
                    existingMember.setPhone(member.getPhone());
                    existingMember.setGender(member.getGender());
                    existingMember.setAddress(member.getAddress());
                    existingMember.setWeight(member.getWeight());
                    existingMember.setHeight(member.getHeight());
                    memberRepository.save(existingMember);
                    return true;
                })
                .orElse(false); // 如果找不到指定的成员，使用map和orElse来简化逻辑
    }

    @Override
    public String changePassword(String memberId, String oldPassword, String newPassword, RedirectAttributes redirectAttributes) {
        Member existingMember = memberRepository.findById(memberId).orElse(null);
        if (existingMember == null) {
            redirectAttributes.addFlashAttribute(ERROR, "Member not found.");
            return "redirect:/member/change-password";
        }
        if (!existingMember.getPassword().equals(oldPassword)) {
            redirectAttributes.addFlashAttribute(ERROR, PASSWORD_INCORRECT);
            return "redirect:/member/update-password";
        }
        if (!isPasswordComplex(newPassword)) {
            redirectAttributes.addFlashAttribute(ERROR, WEAK_PASSWORD);
            return "redirect:/member/update-password";
        }
        existingMember.setPassword(newPassword); // 确保此处使用加密
        memberRepository.save(existingMember);

        redirectAttributes.addFlashAttribute(SUCCESS, "密码已成功更新。");
        return "redirect:/home_page";
    }


    @Override
    public boolean isPasswordComplex(String newPassword) {
        return newPassword.matches("^(?=.*[0-9])(?=.*[a-zA-Z]).{5,}$");
    }

}
