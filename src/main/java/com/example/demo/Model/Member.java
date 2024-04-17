package com.example.demo.Model;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
@Entity
public class Member {
    @Id
    private String member_id;
    private String name;
    private Long phone;
    private String gender;
    private String address;
    private String password;
    private Integer weight;
    private Integer height;
    private Boolean is_purchased;
    private Boolean is_active;
    private Boolean locker_isagreed;

    // Constructors
    public Member() {
    }

    public Member(String member_id, String name, Long phone, String gender, String address, String password, Integer weight, Integer height, Boolean is_purchased, Boolean is_active, Boolean locker_isagreed) {
        this.member_id = member_id;
        this.name = name;
        this.phone = phone;
        this.gender = gender;
        this.address = address;
        this.password = password;
        this.weight = weight;
        this.height = height;
        this.is_purchased = is_purchased;
        this.is_active = is_active;
        this.locker_isagreed = locker_isagreed;
    }

    // Getters and setters
    public String getMember_id() {
        return member_id;
    }

    public void setMember_id(String member_id) {
        this.member_id = member_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getPhone() {
        return phone;
    }

    public void setPhone(Long phone) {
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

    public Boolean getLocker_isagreed() {
        return locker_isagreed;
    }

    public void setLocker_isagreed(Boolean locker_isagreed) {
        this.locker_isagreed = locker_isagreed;
    }
}