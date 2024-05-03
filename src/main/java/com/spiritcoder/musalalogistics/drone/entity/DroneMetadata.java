package com.spiritcoder.musalalogistics.drone.entity;

import com.spiritcoder.musalalogistics.drone.enums.StateEnum;
import com.spiritcoder.musalalogistics.medication.entity.Medication;
import lombok.Data;

import java.util.List;

@Data
public class DroneMetadata {
    private int id;
    private String serialNumber;
    private String model;
    private int weight;
    private int lastBatchId;
    private int currentBatchId;
    private List<Medication> currentBatchList;
    private int currentBatteryLevel;
    private int previousBatteryLevel;
    private StateEnum currentState;
}
