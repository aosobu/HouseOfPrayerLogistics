package com.spiritcoder.musalalogistics.droneservice.repository;


public interface DroneBatteryManager {

    boolean insertDroneBattery(int droneId, byte batteryLevel);
}
