package com.example.demo.Service;

import com.example.demo.Model.Purchase;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface PurchaseService {
    Optional<Purchase> findPurchaseById(String id);
    
    Purchase savePurchase(Purchase purchase);
    void deletePurchase(String id);
    List<Purchase> findAllWithMembers();
}
