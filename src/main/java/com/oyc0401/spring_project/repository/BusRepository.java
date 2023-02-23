package com.oyc0401.spring_project.repository;

import com.oyc0401.spring_project.domain.Bus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface BusRepository {
    Bus save(Bus bus);
    Optional<Bus> findById(Long id);

    List<Bus> findAll();

    Optional<Bus> findFirstByOrderByIdDesc();
    List<Bus> findAllByDepartAtBetween(LocalDateTime start, LocalDateTime end);

//    void update(int id, int busInterval);


}
