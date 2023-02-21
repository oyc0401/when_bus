package com.oyc0401.spring_project.repository;

import com.oyc0401.spring_project.domain.Bus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SpringDataJpaBusRepository extends JpaRepository<Bus, Long>, BusRepository {

    @Override
    Optional<Bus> findByTime(String time);

    @Override
    Optional<Bus> findByBusId(int busId);

}
