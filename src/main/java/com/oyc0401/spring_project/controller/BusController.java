package com.oyc0401.spring_project.controller;

import com.oyc0401.spring_project.domain.Bus;

import com.oyc0401.spring_project.service.BusService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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


    @GetMapping("/bus")
    public String list(Model model){
        List<Bus> buses=busService.findBus();

        model.addAttribute("buses",buses);
        return "buses/busList";
    }

    @GetMapping("/bus/new")
    public String createForm() {
        return "buses/createBusForm";
    }


    @PostMapping("/bus/new")
    @ResponseBody
    public String create() {
        busService.repeat();
        return "추가했습니다.";
    }


//    @GetMapping("api")
//    @ResponseBody
//    public String busApi() {
////        String key = "%2FCX1Je8srsa%2BN1XFaGPVbiGNqbqECXBdN5MYLSf682mak8Po3%2BewTQAuuqybgT6HGAbdv3RLl0%2FqMi32J%2BPbvg%3D%3D";
////        key = "/CX1Je8srsa+N1XFaGPVbiGNqbqECXBdN5MYLSf682mak8Po3+ewTQAuuqybgT6HGAbdv3RLl0/qMi32J+Pbvg==";
////
////        String response = getApi("http://ws.bus.go.kr/api/rest/buspos/getBusPosByRtid?" +
////                "ServiceKey=" + key +
////                "&busRouteId=100100118");
//
//        // 부천 시청역
////        String url="http://ws.bus.go.kr/api/rest/arrive/getArrInfoByRoute?serviceKey=%2FCX1Je8srsa%2BN1XFaGPVbiGNqbqECXBdN5MYLSf682mak8Po3%2BewTQAuuqybgT6HGAbdv3RLl0%2FqMi32J%2BPbvg%3D%3D&stId=210000166&busRouteId=165000154&ord=16&resultType=json";
//
//        // 정석 항공 과학고
//        String url="http://ws.bus.go.kr/api/rest/arrive/getArrInfoByRoute?serviceKey=%2FCX1Je8srsa%2BN1XFaGPVbiGNqbqECXBdN5MYLSf682mak8Po3%2BewTQAuuqybgT6HGAbdv3RLl0%2FqMi32J%2BPbvg%3D%3D&stId=163000168&busRouteId=165000154&ord=2";
//
//        String response = getApi(url);
//
//        JSONObject rjson = new JSONObject(response);
////        System.out.println(rjson.toString());
//
//        JSONObject body= rjson.getJSONObject("msgBody");
//
////        System.out.printf(body.toString());
//
//        JSONArray array= body.getJSONArray("itemList");
//
//        if(!array.isEmpty()){
//            JSONObject object= array.getJSONObject(0);
//            String msg1=object.getString("arrmsg1");
//            int vehId=object.getInt("vehId");
//            if(msg1!="출발대기" ){
//
//            }
//
//        }
//
//
//
//        return  body.toString();
////        return response;
//    }
//
//    private String apiCode() {
//        try {
//            String key = "%2FCX1Je8srsa%2BN1XFaGPVbiGNqbqECXBdN5MYLSf682mak8Po3%2BewTQAuuqybgT6HGAbdv3RLl0%2FqMi32J%2BPbvg%3D%3D";
//            key = "/CX1Je8srsa+N1XFaGPVbiGNqbqECXBdN5MYLSf682mak8Po3+ewTQAuuqybgT6HGAbdv3RLl0/qMi32J+Pbvg==";
//
//
//            StringBuilder urlBuilder = new StringBuilder("http://ws.bus.go.kr/api/rest/buspos/getBusPosByRtid"); /*URL*/
//            urlBuilder.append("?" + URLEncoder.encode("serviceKey", "UTF-8") + "=" + key); /*Service Key*/
//            urlBuilder.append("&" + URLEncoder.encode("busRouteId", "UTF-8") + "=" + URLEncoder.encode("100100118", "UTF-8")); /*노선ID*/
//            URL url = null;
//
//            url = new URL(urlBuilder.toString());
//
//            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//            conn.setRequestMethod("GET");
//            conn.setRequestProperty("Content-type", "application/json");
//            System.out.println("Response code: " + conn.getResponseCode());
//            BufferedReader rd;
//            if (conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
//                rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
//            } else {
//                rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
//            }
//            StringBuilder sb = new StringBuilder();
//            String line;
//            while ((line = rd.readLine()) != null) {
//                sb.append(line);
//            }
//            rd.close();
//            conn.disconnect();
//            System.out.println(sb.toString());
//            return sb.toString();
//        } catch (MalformedURLException e) {
//            throw new RuntimeException(e);
//        } catch (ProtocolException e) {
//            throw new RuntimeException(e);
//        } catch (UnsupportedEncodingException e) {
//            throw new RuntimeException(e);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//
//    }
//
//
//    private String getApi(String urlString) {
//
//
//        try {
//
//            URI uri = new URI(urlString);
//
//            String resp = WebClient.create().get().uri(uri).retrieve().bodyToMono(String.class).block();
//
//            return resp;
//        } catch (URISyntaxException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//
//    @GetMapping("time")
//    @ResponseBody
//    public BusController.Date helloApi() {
//        BusController.Date date = new BusController.Date();
//
//        LocalDateTime now = LocalDateTime.now();
//
//
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS");
//
//        String formattedNow = now.format(formatter);
//
//        date.setTime(formattedNow);
//
//
//        return date;
//    }
//
//    static class Date {
//        private String time;
//
//        public String getTime() {
//            return time;
//        }
//
//        public void setTime(String time) {
//            this.time = time;
//        }
//    }


}
