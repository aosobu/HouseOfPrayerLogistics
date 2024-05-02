package com.spiritcoder.musalalogistics.drone.util;

import com.spiritcoder.musalalogistics.drone.entity.Drone;
import com.spiritcoder.musalalogistics.medication.entity.Medication;

import java.util.List;

public interface DroneLoader {

    List<Medication> loadDrone(List<Medication> medicationList, Drone drone);

}
