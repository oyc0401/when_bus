package com.oyc0401.spring_project.controller;

import com.oyc0401.spring_project.domain.Bus;

import com.oyc0401.spring_project.dto.MessageDto;
import com.oyc0401.spring_project.service.BusService;

import org.springframework.beans.factory.annotation.Autowired;
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

    @GetMapping("/availableTimes")
    @ResponseBody
    public MessageDto getAvailableTime() {
        return new MessageDto(busService.availableTimes());
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

    // 테스트 용도
    @GetMapping("/test/insert")
    @ResponseBody
    public MessageDto insert(@RequestParam("busId") int busId,
                             @RequestParam("departAt") String departAt,
                             @RequestParam("createAt") String createAt,
                             @RequestParam("message") String message,
                             @RequestParam("busNum") String busNum,
                             @RequestParam("isLast") boolean isLast) {
        return new MessageDto(busService.insert(busId, departAt, createAt,message,busNum,isLast));

    }


//    @GetMapping("/test/update")
//    @ResponseBody
//    public void update() {
////        busService.updateIntervalAll();
//
//        List<Bus> list = busService.findAll();
//
//        for (int i = 1; i < list.size(); i++) {
//            Bus late = list.get(i - 1);
//            Bus now = list.get(i);
//
//            Duration duration = Duration.between(late.getDepartAt(), now.getDepartAt());
//
//            System.out.printf(duration.toMinutes() + " <<이거\n");
//            long minutes = duration.toMinutes();
//
//            busService.updateInterval(now.getId(), (int) minutes);
//
//        }
//
//
//    }




}
