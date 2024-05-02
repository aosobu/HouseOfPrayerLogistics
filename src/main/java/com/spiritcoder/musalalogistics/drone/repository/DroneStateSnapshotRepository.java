package com.spiritcoder.musalalogistics.drone.repository;

import com.spiritcoder.musalalogistics.drone.entity.DroneStateSnapshot;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DroneStateSnapshotRepository extends JpaRepository<DroneStateSnapshot, Integer> {

    @Modifying
    @Transactional
    @Query(value = "insert into DroneStateSnapshot(drone, state) values (?1, ?2)", nativeQuery = true)
    void addDroneToDroneStateSnapshot(Integer droneId, String state);

    @Query(value = "select * from DroneStateSnapshot where state in ('IDLE', 'RETURNING')", nativeQuery = true)
    Optional<List<DroneStateSnapshot>> findAllLoadableDrones();

    @Modifying
    @Transactional
    @Query(value = "update DroneStateSnapshot set state  = ?1 where drone = ?2",  nativeQuery = true)
    void updateDroneStateSnapshot(String name, Integer id);

}
