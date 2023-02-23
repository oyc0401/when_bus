package com.oyc0401.spring_project.repository;

import com.oyc0401.spring_project.domain.Bus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface SpringDataJpaBusRepository extends JpaRepository<Bus, Long>, BusRepository {

    Optional<Bus> findFirstByOrderByIdDesc();

    List<Bus> findAllByDepartAtBetween(LocalDateTime start, LocalDateTime end);

//    @Modifying()
//    @Query(value="UPDATE Bus b set b.busInterval= :busInterval  WHERE b.id= :id ", nativeQuery = true)
//    void update(@Param("id") int id,@Param("busInterval") int busInterval);


}
