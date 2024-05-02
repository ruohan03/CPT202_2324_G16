package com.example.demo.repo;

import com.example.demo.Model.Member;
import com.example.demo.Model.Staff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface MemberRepository extends JpaRepository<Member, String> {
    Member findByPhone(String phone); // 根据手机号查找会员
    Member findByPhoneAndName(String phone, String name);
    @Query("select m from Member m where m.endDate <= :thresholdDate and m.endDate > CURRENT_DATE and m.renewalNotificationSent = false")
    List<Member> findMembersForRenewalNotification(Date thresholdDate);
    List<Member> findByRenewalNotificationSent(boolean sent);

    @Query("SELECT m FROM Member m WHERE m.endDate <= :thresholdDate")
    List<Member> findMembersExpiringBefore(Date thresholdDate);

    List<Member> findByPhoneContaining(String phone);
}
