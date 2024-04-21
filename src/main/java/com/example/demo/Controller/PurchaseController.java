package com.example.demo.Controller;

import com.example.demo.Model.Purchase;
import com.example.demo.Service.PurchaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
}
