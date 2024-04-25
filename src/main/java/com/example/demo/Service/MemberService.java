package com.example.demo.Service;

import com.example.demo.Model.Member;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

public interface MemberService {
    List<Member> findAllMembers();

    void addMember(Member member);

    Optional<Member> login(String phoneNumber, String password);

    void saveMember(Member member);

    static Member findByPhone(String phone) {
        return null;
    }

    Member findMemberById(String memberId);

    boolean updateMember(Member member);

    String changePassword(String memberId, String oldPassword, String newPassword, RedirectAttributes redirectAttributes);

    boolean isPasswordComplex(String newPassword);

    static Member findById(String memberId) {
        return null;
    }

    boolean checkMembershipStatus(String memberId);
}
