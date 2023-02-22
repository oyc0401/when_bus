package com.oyc0401.spring_project.repository;

import com.oyc0401.spring_project.domain.Bus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface SpringDataJpaBusRepository extends JpaRepository<Bus, Long>, BusRepository {

    Optional<Bus> findFirstByOrderByIdDesc();

    List<Bus> findAllByDepartAtBetween(LocalDateTime start, LocalDateTime end);

}
