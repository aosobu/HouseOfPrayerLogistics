package com.spiritcoder.musalalogistics.drone.repository;

public interface DroneBatterySnapshotManager {

    boolean addDroneToDroneBatterySnapshot(int batteryLevel, int droneId);

}
