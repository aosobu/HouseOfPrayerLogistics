package com.spiritcoder.musalalogistics.droneservice.repository;

import com.spiritcoder.musalalogistics.droneservice.enums.StateEnum;

public interface DroneStateSnapshotManager {

    boolean updateDroneActivitySnapshot(String name, Integer batchId, Integer id);

    boolean insertDroneActivitySnapshot(StateEnum stateEnum, int droneId);
}
