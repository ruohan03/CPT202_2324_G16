package com.example.demo.Model;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
public class Reservation {

    @Id
    @Column(name = "reservation_id")
    private String reservation_id;

    @Column(name = "member_id")
    private String memberId;

    @Column(name = "staff_id")
    private String staff_id;

    @Column(name = "is_cancelled")
    private boolean isCancelled;

    @Column(name = "is_signin")
    private boolean isSignIn;

    @Column(name = "lesson_start_date")
    private LocalDate lessonStartDate;

    @Column(name = "lesson_start_time")
    private LocalTime lessonStartTime;

    @Column(name = "lesson_end_time")
    private LocalTime lessonEndTime;

    public Reservation() {
    }

    public Reservation(String reservation_id, String memberId, String staff_id, boolean is_cancelled, boolean is_signIn,LocalDate lessonStartDate, LocalTime lessonStartTime, LocalTime lessonEndTime) {
        this.reservation_id = reservation_id;
        this.memberId = memberId;
        this.staff_id = staff_id;
        this.isCancelled = is_cancelled;
        this.isSignIn = is_signIn;
        this.lessonStartDate = lessonStartDate;
        this.lessonStartTime = lessonStartTime;
        this.lessonEndTime = lessonEndTime;
    }

    public String getReservation_id() {
        return reservation_id;
    }

    public void setReservation_id(String reservation_id) {
        this.reservation_id = reservation_id;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getStaff_id() {
        return staff_id;
    }

    public void setStaff_id(String staff_id) {
        this.staff_id = staff_id;
    }

    public boolean getIsCancelled() {return isCancelled;}

    public void setIsCancelled(boolean isCancelled) {
        this.isCancelled = isCancelled;
    }

    public boolean getIsSignIn() {
        return isSignIn;
    }

    public void setIsSignIn(boolean is_signIn) {
        this.isSignIn = is_signIn;
    }

    public LocalDate getLessonStartDate() {return lessonStartDate;}

    public void setLessonStartDate(LocalDate lessonStartDate) {this.lessonStartDate = lessonStartDate;}

    public LocalTime getLessonStartTime() {return lessonStartTime;}

    public void setLessonStartTime(LocalTime lessonStartTime) {this.lessonStartTime = lessonStartTime;}

    public LocalTime getLessonEndTime() {return lessonEndTime;}

    public void setLessonEndTime(LocalTime lessonEndTime) {this.lessonEndTime = lessonEndTime;}

}
