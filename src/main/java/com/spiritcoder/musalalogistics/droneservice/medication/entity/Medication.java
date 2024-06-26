package com.spiritcoder.musalalogistics.droneservice.medication.entity;

import com.spiritcoder.musalalogistics.commons.entity.AbstractAuditable;
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
public class Medication extends AbstractAuditable {

    @Id
    @GeneratedValue
    private Integer id;

    private String name;

    private short weight;

    private String code;

    private byte[] image; // image should not exceed 65kb

    private Integer batch;
}
