package com.spiritcoder.musalalogistics.drone.repository;

public interface DroneStateSnapshotManager {

    boolean addDroneToDroneStateSnapshot(Integer droneId, String state);

}
