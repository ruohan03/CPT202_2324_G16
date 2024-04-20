package com.example.demo.Model;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
public class Reservation {

    @Id
    @Column(name = "reservation_id")
    private String reservation_id;

    @Column(name = "member_id")
    private String member_id;

    @Column(name = "staff_id")
    private String staff_id;

    @Column(name = "lesson_starttime")
    private LocalDateTime lesson_starttime;

    @Column(name = "is_cancelled")
    private boolean is_cancelled;

    @Column(name = "is_signin")
    private boolean is_signin;

    public Reservation() {
    }

    public Reservation(String reservation_id, String member_id, String staff_id, LocalDateTime lesson_starttime, boolean is_cancelled, boolean is_signin) {
        this.reservation_id = reservation_id;
        this.member_id = member_id;
        this.staff_id = staff_id;
        this.lesson_starttime = lesson_starttime;
        this.is_cancelled = is_cancelled;
        this.is_signin = is_signin;
    }

    public String getReservation_id() {
        return reservation_id;
    }

    public void setReservation_id(String reservation_id) {
        this.reservation_id = reservation_id;
    }

    public String getMember_id() {
        return member_id;
    }

    public void setMember_id(String member_id) {
        this.member_id = member_id;
    }

    public String getStaff_id() {
        return staff_id;
    }

    public void setStaff_id(String staff_id) {
        this.staff_id = staff_id;
    }

    public LocalDateTime getLesson_starttime() {
        return lesson_starttime;
    }

    public void setLesson_starttime(LocalDateTime lesson_starttime) {
        this.lesson_starttime = lesson_starttime;
    }

    public boolean isIs_cancelled() {
        return is_cancelled;
    }

    public void setIs_cancelled(boolean is_cancelled) {
        this.is_cancelled = is_cancelled;
    }

    public boolean isIs_signin() {
        return is_signin;
    }

    public void setIs_signin(boolean is_signin) {
        this.is_signin = is_signin;
    }
}
