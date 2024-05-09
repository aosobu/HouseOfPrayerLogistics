package com.spiritcoder.musalalogistics.droneservice.repository;

import com.spiritcoder.musalalogistics.droneservice.entity.DroneActivitySnapshot;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface DroneActivitySnapshotRepository extends JpaRepository<DroneActivitySnapshot, Integer> {

    @Modifying
    @Transactional
    @Query(value = "insert into DroneActivitySnapshot(drone, state, creator, updater, created) values (?1, ?2, ?3, ?4, getDate())", nativeQuery = true)
    void insertDroneActivitySnapshot(Integer droneId, String state, String creator, String updater);

    @Modifying
    @Transactional
    @Query(value = "update DroneActivitySnapshot set state  = ?1, batch = ?2 where drone = ?3",  nativeQuery = true)
    void updateDroneActivitySnapshot(String state, int batch, Integer droneId);

}
