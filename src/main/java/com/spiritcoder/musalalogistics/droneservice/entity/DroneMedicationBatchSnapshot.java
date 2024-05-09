package com.spiritcoder.musalalogistics.droneservice.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@ToString
public class DroneMedicationBatchSnapshot {
    @Id
    @GeneratedValue
    private Integer id;

    private Integer drone;

    private Integer batch;
}
