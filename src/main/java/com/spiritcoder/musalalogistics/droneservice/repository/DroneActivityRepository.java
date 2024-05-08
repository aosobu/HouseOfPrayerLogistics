package com.spiritcoder.musalalogistics.droneservice.repository;

import com.spiritcoder.musalalogistics.droneservice.entity.DroneActivity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface DroneActivityRepository extends JpaRepository<DroneActivity, Integer> {

    @Modifying
    @Transactional
    @Query(value = "insert into DroneActivity(drone, types, batch, creator, updater, created) values (?1, ?2, ?3, ?4, ?5, getDate())", nativeQuery = true)
    void insertDroneActivity(int droneId, String state, Integer batch, String creator, String updater);
}
