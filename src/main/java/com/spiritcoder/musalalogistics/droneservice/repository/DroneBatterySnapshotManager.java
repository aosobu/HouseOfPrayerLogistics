package com.spiritcoder.musalalogistics.droneservice.repository;

public interface DroneBatterySnapshotManager {

    boolean insertDroneBatterySnapshotRecord(int droneId, byte battery);

    byte getBatteryLevel(int droneId);

}
