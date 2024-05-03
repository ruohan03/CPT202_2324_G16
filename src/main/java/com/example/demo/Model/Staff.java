package com.example.demo.Model;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "staff")
public class Staff {
    @Id
    private String staffId;
    private String name;
    private String password;  // 密码字段在Trainer模型中没有，但在Staff中需要
    @Column(name = "encoded_password")
    private String encodedPassword; // 加密密码
    private String phone;
    private String gender;
    private Integer level;
    private Integer salary;
    private String courseId;
    private boolean isManager;  // 管理员状态字段在Trainer模型中没有，但在Staff中需要

    @Column(name = "is_active", nullable = false)
    private boolean isActive;  // 新增字段表示员工是否激活

    // Constructors
    public Staff() {
    }

    public Staff(String staffId, String name, String password, String phone, String gender, int level, int salary, String courseId, boolean isManager, boolean isActive) {
        this.staffId = staffId;
        this.name = name;
        this.password = password;
        this.phone = phone;
        this.gender = gender;
        this.level = level;
        this.salary = salary;
        this.courseId = courseId;
        this.isManager = isManager;
        this.isActive = isActive;
    }

    public Staff(String name) {
        this.name = name;
    }

    // Getters and Setters
    public String getStaffId() {
        return staffId;
    }

    public void setStaffId(String staffId) {
        this.staffId = staffId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEncodedPassword() {return encodedPassword;}

    public void setEncodedPassword(String encodedPassword) {this.encodedPassword = encodedPassword;}

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

    public Integer getLevel() {
        return level != null ? level : 0;  // 如果 level 为 null，则返回一个默认值，如 0
    }


    public void setLevel(int level) {
        this.level = level;
    }

    public Integer getSalary() {
        return salary != null ? salary : 0;  // 如果 level 为 null，则返回一个默认值，如 0
    }

    public void setSalary(int salary) {
        this.salary = salary;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public boolean isManager() {
        return isManager;
    }



    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean isActive) {
        this.isActive = isActive;
    }
}
