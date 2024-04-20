package com.example.demo.repo;

import com.example.demo.Model.Purchase;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Date;

@Repository
public interface PurchaseRepository extends JpaRepository<Purchase, String> {
    // Custom query to find purchases by plan
    List<Purchase> findByPlan(String plan);

    // Additional custom query to find purchases within a specific date range
    List<Purchase> findByPurchaseDateBetween(Date startDate, Date endDate);

    @Query("SELECT p FROM Purchase p JOIN FETCH p.member")
    List<Purchase> findAllWithMember();

}
