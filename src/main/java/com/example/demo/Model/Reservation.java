package com.example.demo.Model;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
public class Reservation {

    @Id
    @Column(name = "reservation_id")
    private String reservationId;

    @Column(name = "member_id")
    private String memberId;

    @Column(name = "staff_id")
    private String staffId;

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

    @Column(name = "created_at")
    private LocalDateTime createdDate;

    public Reservation() {
    }

    public Reservation(String reservationId, String memberId, String staffId, boolean is_cancelled, boolean is_signIn,LocalDate lessonStartDate, LocalTime lessonStartTime, LocalTime lessonEndTime) {
        this.reservationId = reservationId;
        this.memberId = memberId;
        this.staffId = staffId;
        this.isCancelled = is_cancelled;
        this.isSignIn = is_signIn;
        this.lessonStartDate = lessonStartDate;
        this.lessonStartTime = lessonStartTime;
        this.lessonEndTime = lessonEndTime;
    }

    public String getReservationId() {
        return reservationId;
    }

    public void setReservationId(String reservation_id) {
        this.reservationId = reservationId;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getStaffId() {
        return staffId;
    }

    public void setStaffId(String staffId) {
        this.staffId = staffId;
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

    public LocalDateTime getCreatedDate() {return createdDate;}

}
