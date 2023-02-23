package com.oyc0401.spring_project.domain;

import jakarta.persistence.*;

import java.time.LocalDateTime;


@Entity
public class Bus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime departAt;
    private LocalDateTime createAt;
    private int busId;
    private int busInterval;
    private String busNum;
    private String message;
    private boolean isLast;

    public int getBusInterval() {
        return busInterval;
    }

    public void setBusInterval(int busInterval) {
        this.busInterval = busInterval;
    }


    public String getBusNum() {
        return busNum;
    }

    public void setBusNum(String busNum) {
        this.busNum = busNum;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean getIsLast() {
        return isLast;
    }

    public void setIsLast(boolean isLast) {
        this.isLast = isLast;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getDepartAt() {
        return departAt;
    }

    public void setDepartAt(LocalDateTime departAt) {
        this.departAt = departAt;
    }

    public LocalDateTime getCreateAt() {
        return createAt;
    }

    public void setCreateAt(LocalDateTime createAt) {
        this.createAt = createAt;
    }

    public int getBusId() {
        return busId;
    }

    public void setBusId(int busId) {
        this.busId = busId;
    }




}
