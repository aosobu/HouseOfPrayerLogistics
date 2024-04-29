package com.spiritcoder.musalalogistics.drone.repository;

import com.spiritcoder.musalalogistics.drone.entity.DroneStateSnapshot;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface DroneStateSnapshotRepository extends JpaRepository<DroneStateSnapshot, Integer> {

    @Modifying
    @Transactional
    @Query(value = "insert into DroneStateSnapshot(drone, state) values (?1, ?2)", nativeQuery = true)
    void addDroneToDroneStateSnapshot(Integer droneId, String state);

}
