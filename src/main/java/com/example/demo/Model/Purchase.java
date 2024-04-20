package com.example.demo.Model;

import jakarta.persistence.Entity;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "purchase")
public class Purchase {
    @Id
    @Column(name = "purchase_id")
    private String purchaseId;

    @Column(name = "member_id")
    private String memberId;

    @Column(name = "plan")
    private String plan;

    @Column(name = "purchase_date")
    @Temporal(TemporalType.DATE)
    private Date purchaseDate;

    @Column(name = "end_date")
    @Temporal(TemporalType.DATE)
    private Date endDate;

    // Getters
    public String getPurchaseId() {
        return purchaseId;
    }

    public String getMemberId() {
        return memberId;
    }

    public String getPlan() {
        return plan;
    }

    public Date getPurchaseDate() {
        return purchaseDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    // Setters
    public void setPurchaseId(String purchaseId) {
        this.purchaseId = purchaseId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public void setPlan(String plan) {
        this.plan = plan;
    }

    public void setPurchaseDate(Date purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
}
