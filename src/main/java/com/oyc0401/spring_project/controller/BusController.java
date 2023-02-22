package com.oyc0401.spring_project.controller;

import com.oyc0401.spring_project.domain.Bus;

import com.oyc0401.spring_project.dto.MessageDto;
import com.oyc0401.spring_project.service.BusService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class BusController {

    private final BusService busService;

    @Autowired
    public BusController(BusService busService) {
        this.busService = busService;
    }

    @GetMapping("/history")
    @ResponseBody
    public MessageDto getBusHistory(@RequestParam("date") String date) {
        List<Bus> buses = busService.getHistory(date);
        return new MessageDto(buses);
    }

    @PostMapping("/api/start")
    @ResponseBody
    public MessageDto start() {
        return new MessageDto(busService.startRepeat());
    }

    @PostMapping("/api/stop")
    @ResponseBody
    public MessageDto stop() {
        return new MessageDto(busService.stopRepeat());
    }


    @GetMapping("/api/working")
    @ResponseBody
    public MessageDto isWorking() {
        return new MessageDto(busService.isWorking());
    }


    // 테스트 용도
    @GetMapping("/test/allBus")
    @ResponseBody
    public MessageDto getList() {
        return new MessageDto(busService.findAll());

    }

}
