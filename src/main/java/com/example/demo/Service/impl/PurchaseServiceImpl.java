package com.example.demo.Service.impl;

import com.example.demo.Model.Member;
import com.example.demo.Model.Purchase;
import com.example.demo.Service.PurchaseService;
import com.example.demo.repo.MemberRepository;
import com.example.demo.repo.PurchaseRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
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
            System.err.println("Failed to save purchase history: " + e.getMessage());
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
        purchase.setPurchaseDate(new Date()); // 设置购买日期为当前系统日期

        // 获取计划价格
        int price = PurchaseService.getPriceByPlan(purchase.getPlan());

        // 检查计划是否过期
        if (member.getEndDate() != null && new Date().before(member.getEndDate())) {
            // 如果计划未过期，检查新购买的计划是否与现有计划同类
            if (!isSamePlanCategory(member.getPlan(), purchase.getPlan())) {
                throw new IllegalStateException("Cannot purchase a different level plan during the active period.");
            }
        }

        // 检查余额是否足够
        if (member.getRemaining_sum() < price) {
            throw new IllegalStateException("Insufficient balance, please recharge offline.");
        }

        // 扣除购买价格
        member.setRemaining_sum(member.getRemaining_sum() - price);

        Date newEndDate;
        if (member.getPlan() != null && isSamePlanCategory(member.getPlan(), purchase.getPlan())) {
            // 如果续订同一计划，延长有效期
            newEndDate = calculateEndDate(member.getEndDate(), purchase.getPlan());
        } else {
            // 对于新购买或计划更改，从今天开始计算新的有效期
            newEndDate = calculateEndDate(new Date(), purchase.getPlan());
            member.setPlan(normalizePlanName(purchase.getPlan())); // 标准化并更新计划名称
        }

        // 更新结束日期
        member.setEndDate(newEndDate);
        memberRepository.save(member); // 保存会员信息

        // 更新续费通知状态
        if (newEndDate.after(addDays(new Date(), 15))) {
            member.setRenewalNotificationSent(false);
        }

        if (purchase.getPurchaseId() == null || purchase.getPurchaseId().isEmpty()) {
            purchase.setPurchaseId(generatePurchaseId());
        }

        purchase.setEndDate(newEndDate);
        return purchaseRepository.save(purchase); // 保存购买记录
    }

    private Date addDays(Date date, int days) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DAY_OF_MONTH, days);
        return cal.getTime();
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

    // 在 PurchaseService 类中添加以下方法
    public PlanSalesData calculateTotalSalesData(List<Purchase> purchases) {
        if (purchases.isEmpty()) {
            return new PlanSalesData(0, 0);
        }
        double totalSales = purchases.stream()
                .filter(purchase -> purchase.getCost() != null)
                .mapToDouble(Purchase::getCost)
                .sum();
        int totalCount = purchases.size();
        return new PlanSalesData(totalSales, totalCount);
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

    @Override
    public double getThisWeekSales() {
        LocalDate today = LocalDate.now();
        LocalDate startOfWeek = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        LocalDate endOfWeek = today.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));
        Date startOfWeekDate = Date.from(startOfWeek.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date endOfWeekDate = Date.from(endOfWeek.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Double sales = purchaseRepository.sumTotalPriceByDateBetween(startOfWeekDate, endOfWeekDate);
        return (sales != null) ? sales : 0.0;
    }

    @Override
    public double getLastWeekSales() {
        LocalDate today = LocalDate.now();
        LocalDate startOfLastWeek = today.minusWeeks(1).with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        LocalDate endOfLastWeek = today.minusWeeks(1).with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));
        Date startOfLastWeekDate = Date.from(startOfLastWeek.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date endOfLastWeekDate = Date.from(endOfLastWeek.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Double sales = purchaseRepository.sumTotalPriceByDateBetween(startOfLastWeekDate, endOfLastWeekDate);
        return (sales != null) ? sales : 0.0;
    }
}