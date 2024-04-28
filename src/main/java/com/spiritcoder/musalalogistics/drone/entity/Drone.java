package com.spiritcoder.musalalogistics.drone.entity;

import com.spiritcoder.musalalogistics.commons.entity.AbstractAuditable;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Drone  extends AbstractAuditable {
    @Id
    @GeneratedValue
    private Integer id;
    private String serial;
    private String model;
    private int weight;
    private boolean activated;
}
