package com.spiritcoder.musalalogistics.droneservice.repository;

public interface DroneActivityManager {

    boolean insertDroneActivity(int droneId, String state, Integer batch);
}
