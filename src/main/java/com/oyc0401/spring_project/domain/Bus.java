package com.oyc0401.spring_project.domain;

import jakarta.persistence.*;
import org.json.JSONObject;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


@Entity
public class Bus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime departAt;
    private LocalDateTime createAt;
    private int busId;

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


//    static public Bus fromJson(JSONObject jsonObject){
//
//
//        final String msg1 = jsonObject.getString("arrmsg1");
//
//        final int vehId = jsonObject.getInt("vehId1");
//
//        String serverTime = jsonObject.getString("mkTm");
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S");
//        final LocalDateTime serverNow = LocalDateTime.parse(serverTime, formatter);
//
//        System.out.printf("bus(message: "+msg1+", time: "+serverTime+ ")\n");
//
//        Bus newBus = new Bus();
//        newBus.setBusId(vehId);
//        newBus.setDepartAt(roundMinute10(serverNow));
//        newBus.setCreateAt(serverNow);
//
//
//
//        return newBus;
//    }

//    public String toString(){
//        re
//    }



//    static private LocalDateTime roundMinute10(LocalDateTime time) {
//
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:00.000000");
//        String formattedNow = time.format(formatter);
//        LocalDateTime newNow = LocalDateTime.parse(formattedNow, formatter);
//
//        int min = newNow.getMinute() % 10;
//
//        LocalDateTime madeTime = null;
//        if (min > 7) {// 8 9
//            madeTime = newNow.plusMinutes(10 - min);
//        } else {
//            madeTime = newNow.minusMinutes(min);
//        }
//
//        return madeTime;
//    }

}
