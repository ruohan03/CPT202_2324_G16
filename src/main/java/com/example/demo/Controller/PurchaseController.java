package com.example.demo.Controller;

import com.example.demo.Model.Member;
import com.example.demo.Model.Purchase;
import com.example.demo.Service.PurchaseService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Controller
public class PurchaseController {

    @Autowired
    private PurchaseService purchaseService;

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
        String memberId = (String) session.getAttribute("member_id");
        if (memberId == null) {
            return "redirect:/login"; // 如果用户未登录，重定向到登录页面
        }
        model.addAttribute("purchase", new Purchase());
        model.addAttribute("currentPage", "purchaseForm");
        return "purchase-form";
    }

    // 处理购买提交
    @PostMapping("/purchase")
    public String addPurchase(@ModelAttribute Purchase purchase, HttpSession session, RedirectAttributes redirectAttributes) {
        // Retrieve member ID from session
        String memberId = (String) session.getAttribute("member_id");
        if (memberId == null) {
            // If member ID is not available from the session, prompt the user to log in again
            redirectAttributes.addFlashAttribute("error", "Your session has expired, please log in again.");
            return "redirect:/login"; // Redirect to the login page
        }

        try {
            // Create or update purchase information, assuming your service layer has a method to handle create or update logic
            Purchase savedPurchase = purchaseService.createOrUpdatePurchase(purchase, memberId);
            if (savedPurchase != null) {
                redirectAttributes.addFlashAttribute("success", "Purchase successful");
                return "redirect:/purchases"; // If save is successful, redirect to the purchase list page
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


}


