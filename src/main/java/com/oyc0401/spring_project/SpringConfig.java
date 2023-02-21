package com.oyc0401.spring_project;

import com.oyc0401.spring_project.repository.BusRepository;

import com.oyc0401.spring_project.service.BusService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class SpringConfig {

    private final BusRepository busRepository;

    @Autowired
    public SpringConfig(BusRepository busRepository) {
        this.busRepository = busRepository;
    }

    @Bean
    public BusService busService() {
        return new BusService(busRepository);
    }

//    @Bean
//    BusRepository busRepository(){
//
//
//    }

}
