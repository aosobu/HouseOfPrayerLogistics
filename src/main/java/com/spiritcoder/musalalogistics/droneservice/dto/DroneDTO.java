package com.spiritcoder.musalalogistics.droneservice.dto;

import com.spiritcoder.musalalogistics.droneservice.medication.entity.Medication;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class DroneDTO {

    private Integer id;

    private List<Medication> loadedItems;

    private List<DroneRecord> droneRecords;

    private byte batteryLevel;

}
