package com.spiritcoder.musalalogistics.drone.repository;

import com.spiritcoder.musalalogistics.drone.entity.Drone;

import java.util.List;
import java.util.Optional;

public abstract class DroneManager implements DroneStateSnapshotManager, DroneBatterySnapshotManager {

    public abstract Optional<Drone> getDroneBySerialNumber(String serialNumber);

    public abstract Drone getDroneById(Integer id);

    public abstract Optional<List<Drone>> getDroneByModelType(String model);

    public abstract Optional<List<Drone>> getDroneByWeight(Integer weight);

    public abstract Optional<List<Drone>> getAllActiveDrones();

    public abstract Optional<List<Drone>> getAllInActiveDrones();

    public abstract boolean updateActivationStatus(boolean state, Integer id);

}
