package com.example.demo.Service;

import com.example.demo.Model.Purchase;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface PurchaseService {
    List<Purchase> findAllPurchases();
    Optional<Purchase> findPurchaseById(String id);
    
    boolean savePurchase(Purchase purchase);
    void deletePurchase(String id);
    List<Purchase> findPurchasesByPlan(String plan);
    List<Purchase> findPurchasesBetweenDates(Date startDate, Date endDate);

    List<Purchase> findAllWithMembers();

    List<Purchase> findPurchasesByMemberId(String memberId);

    boolean savePurchase(Purchase purchase, String memberId);

    Purchase createOrUpdatePurchase(Purchase purchase, String memberId);



    public static int getPriceByPlan(String plan) {
        return switch (plan) {
            case "Diamond Super VIP" -> 8888;
            case "Gold Super VIP" -> 6888;
            case "Silver Super VIP" -> 3888;
            case "Diamond VIP" -> 5666;
            case "Gold VIP" -> 3666;
            case "Silver VIP" -> 1666;
            default -> 0;
        };
    }

}
