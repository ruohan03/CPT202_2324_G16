package com.example.demo.repo;

import com.example.demo.Model.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, String> {
    // 可以在需要的情况下添加自定义的查询方法
}