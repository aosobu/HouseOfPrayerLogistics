package com.spiritcoder.musalalogistics.droneservice.repository;

import com.spiritcoder.musalalogistics.droneservice.entity.DroneStateType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DroneStateTypeRepository extends JpaRepository<DroneStateType, Integer> {
}
