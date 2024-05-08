package com.spiritcoder.musalalogistics.droneservice.util;

import com.spiritcoder.musalalogistics.droneservice.entity.Drone;
import com.spiritcoder.musalalogistics.droneservice.medication.entity.Medication;

import java.util.List;

public interface DroneLoader {

    List<Medication> loadDrone(List<Medication> medicationList, Drone drone);

}
