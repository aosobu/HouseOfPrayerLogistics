package com.spiritcoder.musalalogistics.medication.entity;

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
public class Medication extends AbstractAuditable {

    @Id
    @GeneratedValue
    private Integer id;

    private String name;

    private short weight;

    private String code;

    private byte[] image; // image should not exceed 65kb

    private int batch;
}
