package com.example.demo.Service.impl;

import com.example.demo.Model.Member;
import com.example.demo.Model.Staff;
import com.example.demo.repo.MemberRepository;
import com.example.demo.repo.StaffRepository;
import com.example.demo.Service.EncryptionService;
import com.example.demo.Service.PasswordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class EncryptionServiceImpl implements EncryptionService {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private StaffRepository staffRepository;

    @Autowired
    private PasswordService passwordService;

    @Transactional
    @Override
    public void encodeAndStoreMemberPasswords() {
        List<Member> members = memberRepository.findAll();
        members.forEach(member -> {
            String encodedPassword = passwordService.encodePassword(member.getPassword());
            member.setEncodedPassword(encodedPassword);
            memberRepository.save(member);
        });
    }

    @Transactional
    @Override
    public void encodeAndStoreStaffPasswords() {
        List<Staff> staff = staffRepository.findAll();
        staff.forEach(st -> {
            String encodedPassword = passwordService.encodePassword(st.getPassword());
            st.setEncodedPassword(encodedPassword);
            staffRepository.save(st);
        });
    }
}
