package com.spiritcoder.musalalogistics.droneservice.entity;

import com.spiritcoder.musalalogistics.commons.entity.AbstractAuditable;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
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
