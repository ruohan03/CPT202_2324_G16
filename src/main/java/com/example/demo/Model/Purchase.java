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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", referencedColumnName = "member_id")
    private Member member;

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

    public Member getMember() {
        return this.member;
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
