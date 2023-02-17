package com.oyc0401.spring_project.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.DefaultUriBuilderFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Controller
public class BusController {


    @GetMapping("api")
    @ResponseBody
    public String busApi() {
        String key = "%2FCX1Je8srsa%2BN1XFaGPVbiGNqbqECXBdN5MYLSf682mak8Po3%2BewTQAuuqybgT6HGAbdv3RLl0%2FqMi32J%2BPbvg%3D%3D";
        key = "/CX1Je8srsa+N1XFaGPVbiGNqbqECXBdN5MYLSf682mak8Po3+ewTQAuuqybgT6HGAbdv3RLl0/qMi32J+Pbvg==";

//        String response = getApi("http://ws.bus.go.kr/api/rest/buspos/getBusPosByRtid?" +
//                "ServiceKey=" + key +
//                "&busRouteId=100100118");

        String response = apiCode();

        return response;
    }

    private String apiCode() {
        try {
            String key = "%2FCX1Je8srsa%2BN1XFaGPVbiGNqbqECXBdN5MYLSf682mak8Po3%2BewTQAuuqybgT6HGAbdv3RLl0%2FqMi32J%2BPbvg%3D%3D";
            key = "/CX1Je8srsa+N1XFaGPVbiGNqbqECXBdN5MYLSf682mak8Po3+ewTQAuuqybgT6HGAbdv3RLl0/qMi32J+Pbvg==";


            StringBuilder urlBuilder = new StringBuilder("http://ws.bus.go.kr/api/rest/buspos/getBusPosByRtid"); /*URL*/
            urlBuilder.append("?" + URLEncoder.encode("serviceKey", "UTF-8") + "=" + key); /*Service Key*/
            urlBuilder.append("&" + URLEncoder.encode("busRouteId", "UTF-8") + "=" + URLEncoder.encode("100100118", "UTF-8")); /*노선ID*/
            URL url = null;

            url = new URL(urlBuilder.toString());

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-type", "application/json");
            System.out.println("Response code: " + conn.getResponseCode());
            BufferedReader rd;
            if (conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
                rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            } else {
                rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
            }
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = rd.readLine()) != null) {
                sb.append(line);
            }
            rd.close();
            conn.disconnect();
            System.out.println(sb.toString());
            return sb.toString();
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (ProtocolException e) {
            throw new RuntimeException(e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }


    private String getApi(String urlString) {

//        String serviceKey = "%2FCX1Je8srsa%2BN1XFaGPVbiGNqbqECXBdN5MYLSf682mak8Po3%2BewTQAuuqybgT6HGAbdv3RLl0%2FqMi32J%2BPbvg%3D%3D";
        String serviceKey = "/CX1Je8srsa+N1XFaGPVbiGNqbqECXBdN5MYLSf682mak8Po3+ewTQAuuqybgT6HGAbdv3RLl0/qMi32J+Pbvg==";

        try {
            //
//            URL url = new URL(urlString);

            URI uri = new URI(urlString);

            String resp = WebClient.create().get().uri(uri).retrieve().bodyToMono(String.class).block();

//            String Base_URL = "http://ws.bus.go.kr/api/rest/buspos";
//
//            DefaultUriBuilderFactory factory = new DefaultUriBuilderFactory(Base_URL);
//            factory.setEncodingMode(DefaultUriBuilderFactory.EncodingMode.VALUES_ONLY);
//
//            WebClient wc = WebClient.builder().uriBuilderFactory(factory).baseUrl(Base_URL).build();
//
//            String response = wc.get()
//                    .uri(uriBuilder -> uriBuilder.path("/getBusPosByRtid")
//                            .queryParam("ServiceKey", serviceKey)
//                            .queryParam("busRouteId", "100100118")
//                            .build()
//                    ).retrieve().bodyToMono(String.class).block();

//            String jsonString = restTemplate.getForObject(uri, String.class);


//            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//
//            int responseCode = connection.getResponseCode();
//
//            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
//            StringBuffer stringBuffer = new StringBuffer();
//            String inputLine;
//
//            while ((inputLine = bufferedReader.readLine()) != null) {
//                stringBuffer.append(inputLine);
//            }
//            bufferedReader.close();
//
//            String response = stringBuffer.toString();

            return resp;
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }


    @GetMapping("time")
    @ResponseBody
    public BusController.Date helloApi() {
        BusController.Date date = new BusController.Date();

        LocalDateTime now = LocalDateTime.now();


        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS");

        String formattedNow = now.format(formatter);

        date.setTime(formattedNow);


        return date;
    }

    static class Date {
        private String time;

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }
    }


}
