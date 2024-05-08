package com.spiritcoder.musalalogistics.droneservice.entity;

import com.spiritcoder.musalalogistics.droneservice.enums.StateEnum;
import com.spiritcoder.musalalogistics.droneservice.medication.entity.Medication;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class DroneMetadata {

    private int id;

    private String serialNumber;

    private String model;

    private int weight;

    private boolean activated;

    private Integer lastBatchId;

    private Integer currentBatchId;

    private List<Medication> currentBatchList;

    private byte currentBatteryLevel;

    private byte lastBatteryLevel;

    private StateEnum currentState;
}
