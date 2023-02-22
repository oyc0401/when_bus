package com.oyc0401.spring_project.controller;

import com.oyc0401.spring_project.domain.Bus;

import com.oyc0401.spring_project.service.BusService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class BusController {

    private final BusService busService;

    @Autowired
    public BusController(BusService busService) {
        this.busService = busService;
    }


    @GetMapping("/all_bus")
    @ResponseBody
    public List<Bus> getList() {
        List<Bus> buses = busService.findBus();

        return buses;
    }

    @GetMapping("/today_bus")
    @ResponseBody
    public List<Bus> getToday() {
        List<Bus> buses = busService.todayBuses();

        return buses;
    }

    @PostMapping("/bus")
    @ResponseBody
    public String insert() {
        busService.insertNow();
        return "추가했습니다.";
    }


    @PostMapping("/bus/api/start")
    @ResponseBody
    public String start() {
        busService.repeat();
        return "시작합니다.";
    }

    @PostMapping("/bus/api/stop")
    @ResponseBody
    public String stop() {
        busService.stop();
        return "정지했습니다.";
    }


    @GetMapping("/bus/api/working")
    @ResponseBody
    public boolean isWorking() {
        boolean working = busService.working();
        return working;
    }



}
