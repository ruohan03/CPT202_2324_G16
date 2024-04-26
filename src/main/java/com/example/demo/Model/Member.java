package com.example.demo.Model;
import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;

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
    private Integer weight;

    @Column
    private Integer height;

    @Column
    private Boolean is_purchased;

    @Column
    private Boolean is_active;

    @Column(name = "locker_is_agreed")
    private Boolean locker_isAgreed;

    @OneToMany(mappedBy = "member")
    private Set<Purchase> purchases;

    @Column(name = "plan")
    private String plan;

    @Column(name = "end_date")
    private Date enddate;

    @Column(name = "remaining_sum", nullable = false)
    private double remaining_sum = 0.0;  // Default value is set to 0.0


    // Constructors
    public Member() {
    }

    public Member(String name, String phone, String gender, String address, String password, Integer weight, Integer height, Boolean is_purchased, Boolean is_active, Boolean locker_isAgreed, String plan, Date enddate) {
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
        this.enddate = enddate;
        this.remaining_sum = remaining_sum;
    }

    public Member(String memberId, String name, String phone, String gender, String address, Integer weight, Integer height, String plan, Date enddate){
        this.memberId = memberId;
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.weight = weight;
        this.height = height;
        this.plan = plan;
        this.enddate = enddate;
    }

    // Getters and setters
    // Include getters and setters for plan and endDate
    public String getPlan() {
        return plan;
    }

    public void setPlan(String plan) {
        this.plan = plan;
    }

    public Date getEndDate() {
        return enddate;
    }

    public void setEndDate(Date enddate) {
        this.enddate = enddate;
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

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
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
