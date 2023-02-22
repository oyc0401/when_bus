package com.oyc0401.spring_project.controller;

import com.oyc0401.spring_project.domain.Bus;

import com.oyc0401.spring_project.service.BusService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class BusController {

    private final BusService busService;

    @Autowired
    public BusController(BusService busService) {
        this.busService = busService;
    }

    @GetMapping("/history")
    @ResponseBody
    public List<Bus> getBusHistory(@RequestParam("date") String date) {
        List<Bus> buses = busService.getHistory(date);
        return buses;
    }

    @PostMapping("/api/start")
    @ResponseBody
    public String start() {
        busService.startRepeat();
        return "시작합니다.";
    }

    @PostMapping("/api/stop")
    @ResponseBody
    public String stop() {
        busService.stopRepeat();
        return "정지했습니다.";
    }


    @GetMapping("/api/working")
    @ResponseBody
    public boolean isWorking() {
        boolean working = busService.isWorking();
        return working;
    }


    // 테스트 용도
    @GetMapping("/test/allBus")
    @ResponseBody
    public List<Bus> getList() {
        List<Bus> buses = busService.findAll();

        return buses;
    }

//    @PostMapping("/test/insert")
//    @ResponseBody
//    public String insert() {
//        busService.insertNow();
//        return "추가했습니다.";
//    }


}
