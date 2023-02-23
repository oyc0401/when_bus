package com.oyc0401.spring_project.service;

import com.oyc0401.spring_project.domain.Bus;
import com.oyc0401.spring_project.repository.BusRepository;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.reactive.function.client.WebClient;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;


public class BusService {

    private final BusRepository busRepository;

    private ExecutorService executorService = Executors.newSingleThreadExecutor();

    private boolean doing = false;

    @Autowired
    public BusService(BusRepository busRepository) {
        this.busRepository = busRepository;
    }

    public boolean isWorking() {
        return doing;
    }

    public String stopRepeat() {
        if(!doing){
            return "이미 작동 중지 되었습니다.";
        }
        doing = false;

        try {
            executorService.shutdown();
//            System.out.printf("쓰레드 종료 1");
            executorService.awaitTermination(20, TimeUnit.SECONDS);
//            System.out.printf("쓰레드 종료 2");
            return "작동 중지 했습니다.";
        } catch (InterruptedException e) {
            System.out.printf(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public String startRepeat() {
        if (doing) {
            return "이미 작동중 입니다.";
//            throw new IllegalStateException("이미 작동중 입니다.");
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
                        JSONObject jsonObject = request1601();
                        checkJoinBus(jsonObject);
                    }


                    // 정지시 1초만에 정지될 수 있도록 지속적으로 확인
                    for (int i = 0; i < 90; i++) {
                        Thread.sleep(1000); // 1초
                        if (!doing) {
                            break;
                        }
                    }


                } catch (InterruptedException e) {
                    System.out.printf(e.getMessage());
                    throw new RuntimeException(e);
                }
            }
            System.out.printf("작업 종료\n");
        });
        return "작동 시작했습니다.";
    }


    public List<Bus> getHistory(String date) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        LocalDate now = LocalDate.parse(date, formatter);
        LocalDateTime startDatetime = LocalDateTime.of(now, LocalTime.of(0, 0, 0));
        LocalDateTime endDatetime = LocalDateTime.of(now, LocalTime.of(23, 59, 59));

        return busRepository.findAllByDepartAtBetween(startDatetime, endDatetime);
    }

    private Long join(Bus bus) {

        try {
            validate(bus);
            busRepository.save(bus);
            System.out.printf("새로운 버스가 출발했습니다!\n");
        } catch (IllegalStateException e) {
            System.out.println(e.getMessage());
        }


        return bus.getId();
    }

    private void validate(Bus bus) {

        Optional<Bus> b = busRepository.findFirstByOrderByIdDesc();
        b.ifPresent(m -> {
            if (m.getBusId() == bus.getBusId()) {
                throw new IllegalStateException("최근에 추가한 버스입니다.");
            }
        });

    }

    private JSONObject request1601() {
        // 부천 시청역
        String urlString = "http://ws.bus.go.kr/api/rest/arrive/getArrInfoByRoute?serviceKey=%2FCX1Je8srsa%2BN1XFaGPVbiGNqbqECXBdN5MYLSf682mak8Po3%2BewTQAuuqybgT6HGAbdv3RLl0%2FqMi32J%2BPbvg%3D%3D&stId=210000166&busRouteId=165000154&ord=16&resultType=json";

        // 정석 항공고
        // String urlString = "http://ws.bus.go.kr/api/rest/arrive/getArrInfoByRoute?serviceKey=%2FCX1Je8srsa%2BN1XFaGPVbiGNqbqECXBdN5MYLSf682mak8Po3%2BewTQAuuqybgT6HGAbdv3RLl0%2FqMi32J%2BPbvg%3D%3D&stId=163000168&busRouteId=165000154&ord=2";

        try {
            URI uri = new URI(urlString);
            String response = WebClient.create().get().uri(uri).retrieve().bodyToMono(String.class).block();
            return new JSONObject(response);
        } catch (URISyntaxException e) {
            System.out.printf(e.getMessage());
            throw new RuntimeException(e);
        }

    }

    private void checkJoinBus(JSONObject rjson) {

        JSONObject body = rjson.getJSONObject("msgBody");
        JSONArray array = body.getJSONArray("itemList");

        if (!array.isEmpty()) {
            JSONObject object = array.getJSONObject(0);

            final String msg1 = object.getString("arrmsg1");
            final int vehId = object.getInt("vehId1");

            final LocalDateTime now = ZonedDateTime.now(ZoneId.of("Asia/Seoul")).toLocalDateTime();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SS");
            System.out.printf("bus(message: " + msg1 + ", time: " + now.format(formatter) + ")\n");
            if (!msg1.equals("출발대기") && !msg1.equals("운행종료")) {

                Bus newBus = new Bus();
                newBus.setBusId(vehId);
                newBus.setDepartAt(roundMinute10(now));
                newBus.setCreateAt(now);

                join(newBus);

            }
        }

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


    // 아래에 있는것들은 테스트 용도

    public String insert(int busId, String departAt, String createAt){

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
        LocalDateTime depart=LocalDateTime.parse(departAt,formatter );
        LocalDateTime create=LocalDateTime.parse(createAt,formatter );

        Bus newBus = new Bus();
        newBus.setBusId(busId);
        newBus.setDepartAt(depart);
        newBus.setCreateAt(create);

        busRepository.save(newBus);

        return "버스를 추가했습니다,";
    }
    public List<Bus> findAll() {
        return busRepository.findAll();
    }

    public void insertNow() {
        Bus newBus = new Bus();
        ZonedDateTime nowSeoul = ZonedDateTime.now(ZoneId.of("Asia/Seoul"));
        LocalDateTime now = nowSeoul.toLocalDateTime();
        LocalDateTime depart = roundMinute10(now);

        newBus.setBusId(1234);
        newBus.setDepartAt(depart);
        newBus.setCreateAt(now);

        busRepository.save(newBus);


    }
}

/**
 * aws 실행하는법
 */

// $ cd /Users/oyuchan/when_bus_key
// $ ssh -i when-bus.pem ubuntu@3.36.184.88

// $ cd spring_study
// $ git pull

// $ sudo chmod +x gradlew
// $ ./gradlew clean build -x test

// $ java -jar ./build/libs/spring_project-0.0.1-SNAPSHOT.jar &

// $ nohup java -jar ./build/libs/spring_project-0.0.1-SNAPSHOT.jar &

// $ curl --location --request POST 'http://3.36.184.88:8080/api/start'

// 현재 실행되는 jar
// $ ps -ef | grep jar

// 중지
// $ kill -9 7483

// 최근에 실행한 nohup 로그
// $ cat nohup.out


/** SQL */

// INSERT INTO BUS VALUES(3, '2023-02-21 00:36:56.442737', 1234);

// DELETE FROM BUS where id=1;
//
//create table bus (
//    id bigint generated by default as identity,
//    bus_id int,
//    depart_at datetime2,
//    create_at datetime2,
//    primary key (id)
//);