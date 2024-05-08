package com.spiritcoder.musalalogistics.droneservice.repository;

import com.spiritcoder.musalalogistics.droneservice.entity.DroneBatterySnapshot;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface DroneBatterySnapshotRepository extends JpaRepository<DroneBatterySnapshot, Integer> {

    @Modifying
    @Transactional
    @Query(value = "insert into DroneBatterySnapshot(battery, drone) values (?1, ?2)",  nativeQuery = true)
    void insertDroneBatterySnapshot(int batteryLevel, int droneId);
}
