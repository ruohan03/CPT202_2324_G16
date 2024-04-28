package com.example.demo.Service.impl;

import com.example.demo.Model.Member;
import com.example.demo.Model.PlanSalesData;
import com.example.demo.Model.Purchase;
import com.example.demo.Service.MemberService;
import com.example.demo.repo.MemberRepository;
import com.example.demo.repo.PurchaseRepository;
import com.example.demo.Service.PurchaseService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

import java.util.List;
import java.util.Optional;
import java.util.Date;
import java.util.stream.Collectors;

@Service
public class PurchaseServiceImpl implements PurchaseService {
    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PurchaseRepository purchaseRepository;
    @Override
    public Optional<Purchase> findPurchaseById(String id) {
        return purchaseRepository.findById(id);
    }

    @Override
    public boolean savePurchase(Purchase purchase) {
        try {
            // 计算结束日期逻辑
            purchase.setEndDate(calculateEndDate(purchase.getPurchaseDate(), purchase.getPlan()));
            purchaseRepository.save(purchase);
            return true;
        } catch (Exception e) {
            System.err.println("保存购买记录失败: " + e.getMessage());
            return false;
        }
    }

    @Override
    public void deletePurchase(String id) {
        purchaseRepository.deleteById(id);
    }

    @Override
    public List<Purchase> findAllWithMembers() {
        return purchaseRepository.findAllWithMember();
    }

    @Override
    public List<Purchase> findPurchasesByMemberId(String memberId) {
        return purchaseRepository.findByMemberId(memberId);
    }

    @Transactional
    public boolean savePurchase(Purchase purchase, String memberId) {
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new RuntimeException("Invalid member ID"));
        purchase.setMember(member);
        purchaseRepository.save(purchase);
        return true;
    }
    @Transactional
    public Purchase createOrUpdatePurchase(Purchase purchase, String memberId) {
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new IllegalArgumentException("Invalid member ID"));
        purchase.setMember(member);

        // Get the price of the plan
        int price = PurchaseService.getPriceByPlan(purchase.getPlan());

        // Check if member has enough balance
        if (member.getRemaining_sum() < price) {
            throw new IllegalStateException("Insufficient balance, please recharge offline.");
        }

        // Deduct the price from member's balance
        member.setRemaining_sum(member.getRemaining_sum() - price);

        Date newEndDate;
        if (member.getPlan() != null && isSamePlanCategory(member.getPlan(), purchase.getPlan())) {
            // If renewing the same plan, extend the validity period
            newEndDate = calculateEndDate(member.getEndDate(), purchase.getPlan());
        } else if (member.getPlan() != null && !isSamePlanCategory(member.getPlan(), purchase.getPlan())) {
            // Throwing exception if trying to purchase a different plan during active plan period
            throw new IllegalStateException("Cannot purchase a different level plan during the active period.");
        } else {
            // For new purchases or plan changes, calculate the new validity period from today
            newEndDate = calculateEndDate(new Date(), purchase.getPlan());
            member.setPlan(normalizePlanName(purchase.getPlan())); // Standardize and update plan name
        }

        member.setEndDate(newEndDate); // Update end date
        memberRepository.save(member); // Save member information

        if (purchase.getPurchaseId() == null || purchase.getPurchaseId().isEmpty()) {
            purchase.setPurchaseId(generatePurchaseId());
        }

        purchase.setEndDate(newEndDate);
        return purchaseRepository.save(purchase); // Save purchase record
    }

    private Purchase getLastActivePurchase(String memberId, String plan) {
        Date currentDate = new Date();  // Get current date
        // Try to fetch the last active purchase record matching the given member ID and plan name from the database
        return purchaseRepository.findLastActivePurchaseByMemberIdAndPlan(memberId, plan, currentDate)
                .orElse(null);  // If not found, return null
    }

    private String normalizePlanName(String plan) {
        if (plan.contains("Diamond")) {
            return "Diamond";
        } else if (plan.contains("Gold")) {
            return "Gold";
        } else if (plan.contains("Silver")) {
            return "Silver";
        }
        return plan;
    }

    private void updateMemberPlanAndEndDate(Member member, String plan, Date endDate) {
        // Unify the member's plan to a basic category name based on the plan name
        if (plan.contains("Diamond")) {
            member.setPlan("Diamond");
        } else if (plan.contains("Gold")) {
            member.setPlan("Gold");
        } else if (plan.contains("Silver")) {
            member.setPlan("Silver");
        } else {
            member.setPlan(plan);  // For other plans not containing the above categories, use the original plan name directly
        }
        member.setEndDate(endDate);
        memberRepository.save(member);
    }

    private Date calculateEndDate(Date currentEndDate, String plan) {
        Calendar cal = Calendar.getInstance();
        if (currentEndDate != null) {
            cal.setTime(currentEndDate);
        } else {
            cal.setTime(new Date()); // If no current end date is available, start calculating from today
        }
        if (plan.contains("Super VIP")) {
            cal.add(Calendar.MONTH, 12); // VIP plan lasts for 12 months
        } else if (plan.contains("VIP")) {
            cal.add(Calendar.MONTH, 6); // Regular member plan lasts for 6 months
        }
        return cal.getTime();
    }

    private String generatePurchaseId() {
        return "P" + UUID.randomUUID().toString().replace("-", "").substring(0, 8);
    }

    private boolean isSamePlanCategory(String memberPlan, String purchasePlan) {
        if (memberPlan == null || purchasePlan == null) {
            return false;
        }
        // Check if it belongs to the Diamond plan
        if ((memberPlan.equals("Diamond") && purchasePlan.contains("Diamond")) ||
                (purchasePlan.equals("Diamond") && memberPlan.contains("Diamond"))) {
            return true;
        }
        // Check if it belongs to the Gold plan
        if ((memberPlan.equals("Gold") && purchasePlan.contains("Gold")) ||
                (purchasePlan.equals("Gold") && memberPlan.contains("Gold"))) {
            return true;
        }
        // Check if it belongs to the Silver plan
        if ((memberPlan.equals("Silver") && purchasePlan.contains("Silver")) ||
                (purchasePlan.equals("Silver") && memberPlan.contains("Silver"))) {
            return true;
        }
        // Default to not the same
        return memberPlan.equals(purchasePlan);
    }

    public List<Purchase> findPurchasesBetweenDates(LocalDate startDate, LocalDate endDate) {
        return purchaseRepository.findByPurchaseDateBetween(startDate, endDate);
    }

    public Map<String, PlanSalesData> calculateSalesData(List<Purchase> purchases) {
        if (purchases == null) {
            return new HashMap<>();
        }

        // 使用Collectors.groupingBy收集器分组，并使用Collectors.summingDouble计算每个组的总销售额
        // 同时使用Collectors.counting计算销售次数
        return purchases.stream()
                .filter(purchase -> purchase.getCost() != null && purchase.getPlan() != null)
                .collect(Collectors.groupingBy(Purchase::getPlan,
                        Collectors.collectingAndThen(Collectors.toList(), list -> {
                            double totalSales = list.stream().mapToDouble(Purchase::getCost).sum();
                            int count = list.size();
                            return new PlanSalesData(totalSales, count);
                        })));
    }

    @Override
    public Map<LocalDate, Map<String, PlanSalesData>> calculateDailySalesByPlan(LocalDate startDate, LocalDate endDate) {
        List<Purchase> purchases = findPurchasesBetweenDates(startDate, endDate);
        return purchases.stream()
                .filter(purchase -> purchase.getCost() != null && purchase.getPlan() != null && purchase.getPurchaseDate() != null)
                .collect(Collectors.groupingBy(
                        purchase -> convertToLocalDateViaCalendar(purchase.getPurchaseDate()),  // 使用转换方法
                        Collectors.groupingBy(
                                Purchase::getPlan,
                                Collectors.collectingAndThen(
                                        Collectors.toList(),
                                        list -> {
                                            double totalSales = list.stream().mapToDouble(Purchase::getCost).sum();
                                            int count = list.size();
                                            return new PlanSalesData(totalSales, count);
                                        }
                                )
                        )
                ));
    }

    private LocalDate convertToLocalDateViaCalendar(Date dateToConvert) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(dateToConvert);
        return LocalDate.of(cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH) + 1,
                cal.get(Calendar.DAY_OF_MONTH));
    }

    public SalesSummary getSalesSummary(List<Purchase> purchases) {
        if (purchases == null || purchases.isEmpty()) {
            return new SalesSummary(0, 0.0, "None", 0);
        }

        int totalSalesCount = purchases.size();
        double totalSalesAmount = purchases.stream()
                .filter(p -> p.getCost() != null)
                .mapToDouble(Purchase::getCost)
                .sum();

        Map<String, Long> planCounts = purchases.stream()
                .filter(p -> p.getPlan() != null)
                .collect(Collectors.groupingBy(Purchase::getPlan, Collectors.counting()));

        String topSellingPlan = planCounts.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("None");
        long topSellingPlanCount = planCounts.getOrDefault(topSellingPlan, 0L);

        return new SalesSummary(totalSalesCount, totalSalesAmount, topSellingPlan, topSellingPlanCount);
    }


}