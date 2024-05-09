package com.spiritcoder.musalalogistics.droneservice.dto;

import com.spiritcoder.musalalogistics.droneservice.medication.entity.Medication;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DroneDTO {

    private Integer id;

    private List<Medication> loadedItems;

}
