package com.example.demo.repo;

import com.example.demo.Model.Purchase;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Date;
import java.util.Optional;

@Repository
public interface PurchaseRepository extends JpaRepository<Purchase, String> {
    @Query("SELECT p FROM Purchase p JOIN FETCH p.member")
    List<Purchase> findAllWithMember();

    // 新增：根据会员ID查找购买记录
    @Query("SELECT p FROM Purchase p WHERE p.member.member_id = :memberId")
    List<Purchase> findByMemberId(@Param("memberId") String memberId);

    @Query("SELECT p FROM Purchase p WHERE p.member.member_id = :memberId AND p.purchaseDate <= :currentDate AND p.endDate >= :currentDate")
    List<Purchase> findActivePurchasesByMemberId(String memberId, Date currentDate);

    @Query("SELECT p FROM Purchase p WHERE p.member.member_id = :memberId AND p.plan = :plan AND p.endDate >= :currentDate ORDER BY p.endDate DESC")
    Optional<Purchase> findLastActivePurchaseByMemberIdAndPlan(String memberId, String plan, Date currentDate);
}
