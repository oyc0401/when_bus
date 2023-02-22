package com.oyc0401.spring_project.service;

import com.oyc0401.spring_project.domain.Bus;
import com.oyc0401.spring_project.repository.BusRepository;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;


public class BusService {

    private final BusRepository busRepository;

    private ExecutorService executorService = Executors.newCachedThreadPool();

    private boolean doing = false;

    @Autowired
    public BusService(BusRepository busRepository) {
        this.busRepository = busRepository;
    }

    /**
     * 회원가입
     *
     * @param bus
     * @return Long
     */
    public Long join(Bus bus) {

        try {
            validate(bus);
            busRepository.save(bus);
        } catch (IllegalStateException e) {
            System.out.println(e.getMessage());
        }


        return bus.getId();
    }

    public void stop() {
        doing = false;
    }

    public boolean working() {
        return doing;
    }

    public void repeat() {
        if (doing) {
            throw new IllegalStateException("이미 작동중 입니다.");
        }
        doing = true;

        // 작업1 (스레드)
        executorService.submit(() -> {
            System.out.printf("작업 시작\n");
            while (doing) {
                try {
                    LocalDateTime now = LocalDateTime.now();
                    int hour = now.getHour();

                    // 0시 ~ 3시면 작동 중지
                    if (!(hour <= 3)) {
                        joinApi();
                    }


                    // 정지시 1초만에 정지될 수 있도록 지속적으로 확인
                    for (int i = 0; i < 90; i++) {
                        Thread.sleep(1000); // 1초
                        if (!doing) {
                            break;
                        }
                    }


                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            System.out.printf("작업 종료\n");
        });

    }

    public void joinApi() {
        // 부천 시청역
        String urlString = "http://ws.bus.go.kr/api/rest/arrive/getArrInfoByRoute?serviceKey=%2FCX1Je8srsa%2BN1XFaGPVbiGNqbqECXBdN5MYLSf682mak8Po3%2BewTQAuuqybgT6HGAbdv3RLl0%2FqMi32J%2BPbvg%3D%3D&stId=210000166&busRouteId=165000154&ord=16&resultType=json";
//        String urlString = "http://ws.bus.go.kr/api/rest/arrive/getArrInfoByRoute?serviceKey=%2FCX1Je8srsa%2BN1XFaGPVbiGNqbqECXBdN5MYLSf682mak8Po3%2BewTQAuuqybgT6HGAbdv3RLl0%2FqMi32J%2BPbvg%3D%3D&stId=163000168&busRouteId=165000154&ord=2";

        try {
            URI uri = new URI(urlString);

            String response = WebClient.create().get().uri(uri).retrieve().bodyToMono(String.class).block();
            JSONObject rjson = new JSONObject(response);
            System.out.printf(rjson.toString() + '\n');

            JSONObject body = rjson.getJSONObject("msgBody");

            JSONArray array = body.getJSONArray("itemList");


            if (!array.isEmpty()) {
                JSONObject object = array.getJSONObject(0);
                String msg1 = object.getString("arrmsg1");
                int vehId = object.getInt("vehId1");


                if (!msg1.equals("출발대기") && !msg1.equals("운행종료")) {
                    Bus newBus = new Bus();
                    LocalDateTime now = LocalDateTime.now();
                    LocalDateTime depart = roundMinute10(now);

                    newBus.setBusId(vehId);
                    newBus.setDepartAt(depart);
                    newBus.setCreateAt(now);

                    join(newBus);

                }

            }

        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }


    }

    private void validate(Bus bus) {

        Optional<Bus> b = busRepository.findFirstByOrderByIdDesc();
        b.ifPresent(m -> {
            if (m.getBusId() == bus.getBusId()) {
                throw new IllegalStateException("최근에 추가한 버스입니다.");
            }
        });

    }

    public void insertNow() {
        Bus newBus = new Bus();
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime depart = roundMinute10(now);

        newBus.setBusId(1234);
        newBus.setDepartAt(depart);
        newBus.setCreateAt(now);

        busRepository.save(newBus);
    }

    private LocalDateTime roundMinute10(LocalDateTime time) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:00.000000");
        String formattedNow = time.format(formatter);
        LocalDateTime newNow = LocalDateTime.parse(formattedNow, formatter);

        int min = newNow.getMinute() % 10;

        LocalDateTime madeTime = null;
        if (min > 7) {// 8 9
            madeTime = newNow.plusMinutes(10 - min);
        } else {
            madeTime = newNow.minusMinutes(min);
        }

        return madeTime;
    }

    public List<Bus> findBus() {
        return busRepository.findAll();
    }

    public List<Bus> todayBuses() {

        LocalDateTime startDatetime = LocalDateTime.of(LocalDate.now(), LocalTime.of(0, 0, 0));
        LocalDateTime endDatetime = LocalDateTime.of(LocalDate.now(), LocalTime.of(23, 59, 59));

        return busRepository.findAllByDepartAtBetween(startDatetime, endDatetime);
    }


    public Optional<Bus> findOne(Long id) {
        return busRepository.findById(id);
    }
}

// INSERT INTO BUS VALUES(3, '2023-02-21 00:36:56.442737', 1234);

// DELETE FROM BUS where id=1;

//create table bus (
//    id bigint generated by default as identity,
//    bus_id int,
//    depart_at datetime2,
//    create_at datetime2,
//    primary key (id)
//);