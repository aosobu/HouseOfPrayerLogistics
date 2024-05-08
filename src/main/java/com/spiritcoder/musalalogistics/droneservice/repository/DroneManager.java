package com.spiritcoder.musalalogistics.droneservice.repository;

import com.spiritcoder.musalalogistics.droneservice.entity.Drone;
import com.spiritcoder.musalalogistics.droneservice.enums.ModelEnum;

import java.util.List;
import java.util.Optional;

public abstract class DroneManager implements DroneStateSnapshotManager, DroneBatterySnapshotManager, DroneBatteryManager, DroneActivityManager {

    public abstract Drone getDroneById(Integer id);

    public abstract Optional<List<Drone>> getAllInActiveDrones();

    public abstract boolean updateActivationStatus(boolean state, Integer id);

    public abstract boolean insertDrone(String serialNumber, ModelEnum model, short weight);

    public abstract Optional<Drone> findDroneBySerialNumber(String serialNumber);
}
