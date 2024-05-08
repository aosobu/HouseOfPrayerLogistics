package com.spiritcoder.musalalogistics.droneservice.repository;

import com.spiritcoder.musalalogistics.droneservice.entity.DroneActivitySnapshot;
import com.spiritcoder.musalalogistics.droneservice.enums.StateEnum;

import java.util.List;
import java.util.Optional;

public interface DroneStateSnapshotManager {

    Optional<List<DroneActivitySnapshot>> findAllLoadableDrones();

    boolean updateDroneStateSnapshot(String name, Integer id);

    boolean insertDroneActivitySnapshot(StateEnum stateEnum, int droneId);
}
