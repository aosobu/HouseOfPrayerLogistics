package com.spiritcoder.musalalogistics.droneservice.entity;

import com.spiritcoder.musalalogistics.commons.entity.AbstractAuditable;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@AllArgsConstructor
@Entity
public class DroneStateType extends AbstractAuditable {
    @Id
    @GeneratedValue
    private Integer id;
    private String type;
}
