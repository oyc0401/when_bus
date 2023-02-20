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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

import static java.rmi.server.LogStream.log;

@Service
public class BusService {

    private final BusRepository busRepository;

    private ExecutorService executorService = Executors.newCachedThreadPool();


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

        validate(bus);

        busRepository.save(bus);
        return bus.getId();
    }

    public void repeat() {
//        if(!executorService.isTerminated()){
//            executorService.shutdown();
//
//        }

        // 작업1 (스레드)
        executorService.submit(() -> {
            System.out.printf("작업 시작\n");
            for (int i = 0; i <= 30; i++) {
                try {
                    joinApi();
                    Thread.sleep(1000*60);

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
//        System.out.println(rjson.toString());

            JSONObject body = rjson.getJSONObject("msgBody");

            System.out.printf(body.toString()+'\n');

            JSONArray array = body.getJSONArray("itemList");


            if (!array.isEmpty()) {
                JSONObject object = array.getJSONObject(0);
                String msg1 = object.getString("arrmsg1");
                int vehId = object.getInt("vehId1");

                LocalDateTime now = LocalDateTime.now();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS");
                String formattedNow = now.format(formatter);

                if (!msg1.equals("출발대기")) {
                    Bus newBus = new Bus();
                    newBus.setTime(formattedNow);
                    newBus.setBusId(vehId);

                    join(newBus);

                }

            }

        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }


    }

    private boolean validate(Bus bus) {
        AtomicBoolean can= new AtomicBoolean(true);
        busRepository.findByBusId(bus.getBusId())
                .ifPresent(m -> {
                    can.set(false);
//                    throw new IllegalStateException("이미 존재하는 버스입니다.");
                });
        return can.get();
    }

    public List<Bus> findBus() {
        return busRepository.findAll();
    }

    public Optional<Bus> findOne(Long memberId) {
        return busRepository.findById(memberId);
    }
}
