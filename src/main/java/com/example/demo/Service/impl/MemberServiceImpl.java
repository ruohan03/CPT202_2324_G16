package com.example.demo.Service.impl;

import com.example.demo.Model.Member;
import com.example.demo.Model.Staff;
import com.example.demo.Service.MemberService;
import com.example.demo.Service.PasswordService;
import com.example.demo.exception.MembershipValidationException;
import com.example.demo.repo.MemberRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static com.example.demo.util.Constants.*;

@Service
public class MemberServiceImpl implements MemberService {
    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PasswordService passwordService;

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
        if (member != null && passwordService.matchPassword(password, member.getEncodedPassword())) {
            return Optional.of(member); // 如果电话号码和密码匹配，则返回Optional包含Member对象
        }
        return Optional.empty(); // 如果密码不匹配或用户不存在，则返回一个空的Optional
    }

    @Transactional
    @Override
    public void saveMember(Member member) {
        // 检查电话号码是否已提供，并尝试生成基于电话号码的 memberId
        if (member.getPhone() != null && !member.getPhone().isEmpty()) {
            String proposedId = "M" + member.getPhone();
            // 检查是否已存在相同的 memberId
            if (memberRepository.existsById(proposedId)) {
                // 如果已存在，处理冲突（例如抛出异常或生成一个不同的 ID）
                throw new IllegalStateException("Member ID already exists for this phone number.");
            }
            member.setMemberId(proposedId);
        } else {
            // 如果电话号码没有提供，可以选择抛出异常或采取其他措施
            throw new IllegalArgumentException("Phone number is required to generate member ID.");
        }

        // 保存会员信息到数据库
        memberRepository.save(member);
    }

    @Override
    public Member findMemberById(String memberId) {
        return memberRepository.findById(memberId).orElse(null);
    }

    @Transactional
    @Override
    public boolean updateMember(Member member) {
        return memberRepository.findById(member.getMemberId())
                .map(existingMember -> {
                    // 仅更新非null且表单中提供的字段
                    if (member.getName() != null) existingMember.setName(member.getName());
                    if (member.getPhone() != null) existingMember.setPhone(member.getPhone());
                    if (member.getGender() != null) existingMember.setGender(member.getGender());
                    if (member.getAddress() != null) existingMember.setAddress(member.getAddress());
                    if (member.getWeight() != null) existingMember.setWeight(member.getWeight());
                    if (member.getHeight() != null) existingMember.setHeight(member.getHeight());
                    // 保存更新后的对象
                    memberRepository.save(existingMember);
                    return true;
                })
                .orElse(false); // 如果找不到指定的成员，返回false
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
        existingMember.setPassword(newPassword);
        existingMember.setEncodedPassword(passwordService.encodePassword(newPassword));// 确保此处使用加密
        memberRepository.save(existingMember);

        redirectAttributes.addFlashAttribute(SUCCESS, "The password has been successfully updated.");
        return "redirect:/home_page";
    }


    @Override
    public boolean isPasswordComplex(String newPassword) {
        return newPassword.matches("^(?=.*[0-9])(?=.*[a-zA-Z]).{5,}$");
    }

    @Override
    public boolean checkMembershipStatus(String memberId) throws MembershipValidationException {
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new MembershipValidationException("No member information found."));
        if (member.getPlan() == null) {
            throw new MembershipValidationException("You have not purchased any plans yet.");
        }
        if (member.getEndDate() != null && new Date().after(member.getEndDate())) {
            throw new MembershipValidationException("Your membership plan has expired.");
        }
        if(member.getEndDate()==null){
            throw new MembershipValidationException("Something wrong happens, please connect with the administrator.");
        }
        return true; // 所有检查都通过
    }

    @Override
    public Member findByPhoneAndName(String phone, String name) {
        return memberRepository.findByPhoneAndName(phone, name);
    }

    @Override
    public boolean updatePassword(String phone, String newPassword) {
        Member member = memberRepository.findByPhone(phone);
        if (member != null) {
            member.setPassword(newPassword);
            memberRepository.save(member);
            return true;
        }
        return false;
    }

    @Override
    public Member findMemberByPhone(String phone) {
        return memberRepository.findByPhone(phone);
    }

    @Override
    public void rechargeMember(String memberId, double amount) {
        // 先根据ID查找用户
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("No member found with ID: " + memberId));
        // 更新余额
        double newBalance = member.getRemaining_sum() + amount;
        member.setRemaining_sum(newBalance);
        memberRepository.save(member);
    }

    @Override
    public List<Member> findMemberByPhoneContaining(String phone) {
        return memberRepository.findByPhoneContaining(phone);
    }

}
