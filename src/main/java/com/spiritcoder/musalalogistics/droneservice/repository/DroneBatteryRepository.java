package com.spiritcoder.musalalogistics.droneservice.repository;

import com.spiritcoder.musalalogistics.droneservice.entity.DroneBattery;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface DroneBatteryRepository extends JpaRepository<DroneBattery, Integer> {

    @Modifying
    @Transactional
    @Query(value = "insert into DroneBattery(drone, battery, creator, updater, created) values (?1, ?2, ?3, ?4, getDate())",  nativeQuery = true)
    void insertDroneBattery(int droneId, byte battery, String creator, String updater);
}
