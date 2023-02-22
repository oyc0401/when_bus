package com.oyc0401.spring_project.service;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.*;

class BusServiceTest {

    @Test
    void getHistory() {

        String date = "20230403";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        LocalDate now = LocalDate.parse(date,formatter);
        LocalDateTime startDatetime = LocalDateTime.of(now, LocalTime.of(0, 0, 0));
        LocalDateTime endDatetime = LocalDateTime.of(now, LocalTime.of(23, 59, 59));


        System.out.printf(startDatetime.toString()+'\n');
    }
}