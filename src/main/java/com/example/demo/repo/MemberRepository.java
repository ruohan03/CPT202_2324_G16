package com.example.demo.repo;

import com.example.demo.Model.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;



@Repository
public interface MemberRepository extends JpaRepository<Member, String> {
    Member findByPhone(String phone); // 根据手机号查找会员

}