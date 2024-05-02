package com.example.demo.Model;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.Date;
import java.util.Set;

@Entity
public class Member {
    @Id
    @Column(name = "member_id")
    private String memberId;

    @Column(name = "name")
    private String name;

    @Column(name = "phone")
    private String phone;

    @Column
    private String gender;

    @Column
    private String address;

    @Column
    private String password;

    @Column(name = "encoded_Password")
    private String encodedPassword;

    @Column
    private Double weight;

    @Column
    private Double height;

    @Column
    private Boolean is_purchased;

    @Column
    private Boolean is_active;

    @Column(name = "locker_is_agreed")
    private Boolean locker_isAgreed;

    @OneToMany(mappedBy = "member")
    @JsonIgnore
    private Set<Purchase> purchases;

    @Column(name = "plan")
    private String plan;

    @Column(name = "end_date")
    private Date endDate;

    @Column(name = "remaining_sum", nullable = false)
    private double remaining_sum = 0.0;  // Default value is set to 0.0

    @Column(name = "renewal_notification_sent")
    private boolean renewalNotificationSent = false;

    public boolean isRenewalNotificationSent() {
        return renewalNotificationSent;
    }

    public void setRenewalNotificationSent(boolean renewalNotificationSent) {
        this.renewalNotificationSent = renewalNotificationSent;
    }

    // Constructors
    public Member() {
    }

    public Member(String name, String phone, String gender, String address, String password, Double weight, Double height, Boolean is_purchased, Boolean is_active, Boolean locker_isAgreed, String plan, Date endDate) {
        this.memberId = "M" + phone;
        this.name = name;
        this.phone = phone;
        this.gender = gender;
        this.address = address;
        this.password = password;
        this.weight = weight;
        this.height = height;
        this.is_purchased = is_purchased;
        this.is_active = is_active;
        this.locker_isAgreed = locker_isAgreed;
        this.plan = plan;
        this.endDate = endDate;
        this.remaining_sum = remaining_sum;
    }

    public Member(String memberId, String name, String phone, String gender, String address, Double weight, Double height, String plan, Date endDate){
        this.memberId = memberId;
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.weight = weight;
        this.height = height;
        this.plan = plan;
        this.endDate = endDate;
    }

    // Getters and setters
    // Include getters and setters for plan and enddate
    public String getPlan() {
        return plan;
    }

    public void setPlan(String plan) {
        this.plan = plan;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date enddate) {
        this.endDate = enddate;
    }

    // Existing getters and setters
    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public Set<Purchase> getPurchases() {
        return purchases;
    }

    public void setPurchases(Set<Purchase> purchases) {
        this.purchases = purchases;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEncodedPassword() {return encodedPassword;}

    public void setEncodedPassword(String encodedPassword) {this.encodedPassword = encodedPassword;}

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public Double getHeight() {
        return height;
    }

    public void setHeight(Double height) {
        this.height = height;
    }

    public Boolean getIs_purchased() {
        return is_purchased;
    }

    public void setIs_purchased(Boolean is_purchased) {
        this.is_purchased = is_purchased;
    }

    public Boolean getIs_active() {
        return is_active;
    }

    public void setIs_active(Boolean is_active) {
        this.is_active = is_active;
    }

    public Boolean getLocker_isAgreed() {
        return locker_isAgreed;
    }

    public void setLocker_isAgreed(Boolean locker_isAgreed) {
        this.locker_isAgreed = locker_isAgreed;
    }

    public double getRemaining_sum() {
        return remaining_sum;
    }

    public void setRemaining_sum(double remaining_sum) {
        this.remaining_sum = remaining_sum;
    }
}
