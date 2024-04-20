package com.example.demo.Service.impl;

import com.example.demo.Model.Purchase;
import com.example.demo.repo.PurchaseRepository;
import com.example.demo.Service.PurchaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class PurchaseServiceImpl implements PurchaseService {

    @Autowired
    private PurchaseRepository purchaseRepository;

    @Override
    public List<Purchase> findAllPurchases() {
        return purchaseRepository.findAll();
    }

    @Override
    public Optional<Purchase> findPurchaseById(String id) {
        return purchaseRepository.findById(id);
    }

    @Override
    public Purchase savePurchase(Purchase purchase) {
        return purchaseRepository.save(purchase);
    }

    @Override
    public void deletePurchase(String id) {
        purchaseRepository.deleteById(id);
    }

    @Override
    public List<Purchase> findPurchasesByPlan(String plan) {
        return purchaseRepository.findByPlan(plan);
    }

    @Override
    public List<Purchase> findPurchasesBetweenDates(Date startDate, Date endDate) {
        return purchaseRepository.findByPurchaseDateBetween(startDate, endDate);
    }
}
