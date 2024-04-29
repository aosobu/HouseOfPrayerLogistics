package com.spiritcoder.musalalogistics.drone.repository;

import com.spiritcoder.musalalogistics.drone.entity.DroneStateSnapshot;

import java.util.List;
import java.util.Optional;

public interface DroneStateSnapshotManager {

    boolean addDroneToDroneStateSnapshot(Integer droneId, String state);

    Optional<List<DroneStateSnapshot>> findAllLoadableDrones();

    boolean updateDroneStateSnapshot(String name, Integer id);
}
