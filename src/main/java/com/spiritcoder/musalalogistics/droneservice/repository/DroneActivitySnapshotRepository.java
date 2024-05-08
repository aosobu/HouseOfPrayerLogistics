package com.spiritcoder.musalalogistics.droneservice.repository;

import com.spiritcoder.musalalogistics.droneservice.entity.DroneActivitySnapshot;
import com.spiritcoder.musalalogistics.droneservice.enums.StateEnum;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DroneActivitySnapshotRepository extends JpaRepository<DroneActivitySnapshot, Integer> {

    @Modifying
    @Transactional
    @Query(value = "insert into DroneActivitySnapshot(drone, state) values (?1, ?2)", nativeQuery = true)
    void insertDroneActivitySnapshot(Integer droneId, StateEnum state);

    @Query(value = "select * from DroneActivitySnapshot where state in ('IDLE')", nativeQuery = true)
    Optional<List<DroneActivitySnapshot>> findAllLoadableDrones();

    @Modifying
    @Transactional
    @Query(value = "update DroneActivitySnapshot set state  = ?1 where drone = ?2",  nativeQuery = true)
    void updateDroneActivitySnapshot(String name, Integer id);

}
