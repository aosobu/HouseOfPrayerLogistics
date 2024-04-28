package com.spiritcoder.musalalogistics.drone.repository;

import com.spiritcoder.musalalogistics.drone.entity.Drone;

import java.util.List;
import java.util.Optional;

public interface DroneRepositoryManager {
    Optional<Drone> getDroneBySerialNumber(String serialNumber);
    Optional<Drone> getDroneById(Integer id);
    Optional<List<Drone>> getDroneByModelType(String model);
    Optional<List<Drone>> getDroneByWeight(Integer weight);
    Optional<List<Drone>> getAllActiveDrones();
    Optional<List<Drone>> getAllInActiveDrones();
}
