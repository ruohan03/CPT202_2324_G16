package com.example.demo.Service;

import com.example.demo.Model.PlanSalesData;
import com.example.demo.Model.Purchase;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface PurchaseService {

    public static class SalesSummary {
        private int totalSalesCount;
        private double totalSalesAmount;
        private String topSellingPlan;
        private long topSellingPlanCount;

        public SalesSummary(int totalSalesCount, double totalSalesAmount, String topSellingPlan, long topSellingPlanCount) {
            this.totalSalesCount = totalSalesCount;
            this.totalSalesAmount = totalSalesAmount;
            this.topSellingPlan = topSellingPlan;
            this.topSellingPlanCount = topSellingPlanCount;
        }

        public int getTotalSalesCount() {
            return totalSalesCount;
        }

        public double getTotalSalesAmount() {
            return totalSalesAmount;
        }

        public String getTopSellingPlan() {
            return topSellingPlan;
        }

        public long getTopSellingPlanCount() {
            return topSellingPlanCount;
        }
    }


    public static class PlanSalesData {
        private double totalSales;
        private int count;

        public PlanSalesData(double totalSales, int count) {
            this.totalSales = totalSales;
            this.count = count;
        }

        public double getTotalSales() {
            return totalSales;
        }

        public int getCount() {
            return count;
        }
    }
    Optional<Purchase> findPurchaseById(String id);
    
    boolean savePurchase(Purchase purchase);
    void deletePurchase(String id);
    List<Purchase> findAllWithMembers();

    List<Purchase> findPurchasesByMemberId(String memberId);

    boolean savePurchase(Purchase purchase, String memberId);

    Purchase createOrUpdatePurchase(Purchase purchase, String memberId);

    List<Purchase> findPurchasesBetweenDates(LocalDate startDate, LocalDate endDate);

    // 新增：计算不同计划的销售数据
    Map<String, PlanSalesData> calculateSalesData(List<Purchase> purchases);

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

    Map<LocalDate, Map<String, PlanSalesData>> calculateDailySalesByPlan(LocalDate startDate, LocalDate endDate);

    SalesSummary getSalesSummary(List<Purchase> purchases);


    PlanSalesData calculateTotalSalesData(List<Purchase> purchases);
}
