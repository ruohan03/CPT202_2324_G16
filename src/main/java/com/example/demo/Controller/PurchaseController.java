package com.example.demo.Controller;

import com.example.demo.Model.Purchase;
import com.example.demo.Service.MemberService;
import com.example.demo.Service.PdfReportService;
import com.example.demo.Service.PurchaseService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Controller
public class PurchaseController {

    @Autowired
    private PurchaseService purchaseService;

    @Autowired
    private MemberService memberService;
    @Autowired
    private PdfReportService pdfReportService;

    @GetMapping("/purchases")
    public String listPurchases(Model model) {
        // 直接从服务层获取所有购买记录，并确保包括关联的会员信息
        List<Purchase> purchases = purchaseService.findAllWithMembers();
        model.addAttribute("purchases", purchases);
        return "purchase-list"; // Thymeleaf模板的名称
    }

    @GetMapping("/purchases/{id}")
    public String getPurchase(@PathVariable String id, Model model) {
        Purchase purchase = purchaseService.findPurchaseById(id).orElse(null);
        model.addAttribute("purchase", purchase);
        return "purchase-list"; // 返回purchase-detail.html模板
    }

    @PostMapping("/purchases")
    public String addPurchase(@ModelAttribute Purchase purchase) {
        purchaseService.savePurchase(purchase);
        return "redirect:/purchases"; // 保存后重定向到列表页面
    }

    @GetMapping("/purchases/delete/{id}")
    public String deletePurchase(@PathVariable String id) {
        purchaseService.deletePurchase(id);
        return "redirect:/purchases"; // 删除后重定向到列表页面
    }

    // 显示购买页面
    @GetMapping("/purchase/new")
    public String showPurchaseForm(HttpSession session, Model model) {
        String memberId = (String) session.getAttribute("memberId");
        if (memberId == null) {
            return "redirect:/login"; // 如果用户未登录，重定向到登录页面
        }
        model.addAttribute("purchase", new Purchase());
        model.addAttribute("member", memberService.findMemberById(memberId));
        model.addAttribute("isLoggedIn", true);
        model.addAttribute("currentPage", "purchaseForm");
        return "purchase-form";
    }

    // 处理购买提交
    @PostMapping("/purchase")
    public String addPurchase(@ModelAttribute Purchase purchase, HttpSession session, RedirectAttributes redirectAttributes) {
        String memberId = (String) session.getAttribute("memberId");
        if (memberId == null) {
            redirectAttributes.addFlashAttribute("error", "Your session has expired, please log in again.");
            return "redirect:/login"; // Redirect to the login page
        }

        try {
            purchase.setPurchaseDate(new Date()); // Set the purchase date to the current date here

            int cost = PurchaseService.getPriceByPlan(purchase.getPlan());
            purchase.setCost((double) cost); // Convert int to Double and set the cost of the purchase

            Purchase savedPurchase = purchaseService.createOrUpdatePurchase(purchase, memberId);
            if (savedPurchase != null) {
                redirectAttributes.addFlashAttribute("success", "Purchase successful");
                return "redirect:/home_page"; // If save is successful, redirect to the purchase list page
            } else {
                redirectAttributes.addFlashAttribute("error", "Failed to process purchase, please try again.");
                return "redirect:/purchase/new"; // If processing fails, redirect back to the purchase form page
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "An error occurred while processing the purchase: " + e.getMessage());
            return "redirect:/purchase/new"; // In case of an exception, redirect back to the purchase form page
        }
    }


    @InitBinder  // Used for customizing the binding of data from front end to back end
    public void initBinder(WebDataBinder binder) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd"); // Determine your date format
        dateFormat.setLenient(false);  // Set not to parse dates leniently, i.e., strictly parse according to the format above
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true)); // Register the editor
    }


    // 用户查看自己的购买记录
    @GetMapping("/user/purchases")
    public String listUserPurchases(Model model, HttpSession session) {
        String memberId = (String) session.getAttribute("memberId");
        if (memberId == null) {
            return "redirect:/login"; // 未登录则跳转到登录页
        }
        List<Purchase> purchases = purchaseService.findPurchasesByMemberId(memberId);
        model.addAttribute("purchases", purchases);
        return "user-purchases"; // 返回用户专用的购买记录模板
    }


    @GetMapping("/admin/purchases")
    public String listAllPurchases(HttpSession session, Model model) {
        List<Purchase> purchases = purchaseService.findAllWithMembers();
        model.addAttribute("purchases", purchases);
        model.addAttribute("currentPage", "manager_purchases");
        return "manager-purchases"; // 返回管理员专用的购买记录模板
    }

    @GetMapping("/admin/reports")
    public String showReportForm(Model model) {
        model.addAttribute("currentPage", "sales_report");
        return "report_form"; // 返回用于输入日期范围的表单页面
    }

    @PostMapping("/admin/reports")
    public String generateReport(@RequestParam("startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
                                 @RequestParam("endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate,
                                 Model model) {
        List<Purchase> purchases = purchaseService.findPurchasesBetweenDates(startDate, endDate);
        Map<String, PurchaseService.PlanSalesData> salesData = purchaseService.calculateSalesData(purchases);
        Map<LocalDate, Map<String, PurchaseService.PlanSalesData>> dailySalesData = purchaseService.calculateDailySalesByPlan(startDate, endDate);
        PurchaseService.PlanSalesData totalSalesData = purchaseService.calculateTotalSalesData(purchases);

        model.addAttribute("salesData", salesData);
        model.addAttribute("dailySalesData", dailySalesData);
        model.addAttribute("totalSales", totalSalesData.getTotalSales());
        model.addAttribute("totalCount", totalSalesData.getCount());
        model.addAttribute("currentPage", "sales_report");
        return "sales_report";  // 确保这个视图模板正确处理新的数据结构
    }

    @GetMapping("/exportPdf")
    public String exportToPdf(@RequestParam("startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
                              @RequestParam("endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate,
                              HttpServletResponse response, Model model) throws IOException {
        if (startDate == null || endDate == null) {
            model.addAttribute("error", "Please select both start and end dates.");
            return "report_form";
        }

        List<Purchase> purchases = purchaseService.findPurchasesBetweenDates(startDate, endDate);
        Map<String, PurchaseService.PlanSalesData> salesData = purchaseService.calculateSalesData(purchases);
        Map<LocalDate, Map<String, PurchaseService.PlanSalesData>> dailySalesData = purchaseService.calculateDailySalesByPlan(startDate, endDate);
        PurchaseService.PlanSalesData totalSalesData = purchaseService.calculateTotalSalesData(purchases);

        pdfReportService.generatePdfReport(salesData, dailySalesData, totalSalesData, response);
        return null;  // 因为 response 已经处理了输出
    }


    @GetMapping("/exportChartPdf")
    public String exportChartToPdf(@RequestParam("startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
                                   @RequestParam("endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate,
                                   HttpServletResponse response, Model model) throws IOException {
        if (startDate == null || endDate == null) {
            model.addAttribute("error", "Please select both start and end dates.");
            return "report_form";
        }

        List<Purchase> purchases = purchaseService.findPurchasesBetweenDates(startDate, endDate);
        Map<String, PurchaseService.PlanSalesData> salesData = purchaseService.calculateSalesData(purchases);
        Map<LocalDate, Map<String, PurchaseService.PlanSalesData>> dailySalesData = purchaseService.calculateDailySalesByPlan(startDate, endDate);
        PurchaseService.PlanSalesData totalSalesData = purchaseService.calculateTotalSalesData(purchases);

        pdfReportService.generateChartPdfReport(salesData, dailySalesData, totalSalesData, response);
        return null;  // 因为 response 已经处理了输出
    }

}


