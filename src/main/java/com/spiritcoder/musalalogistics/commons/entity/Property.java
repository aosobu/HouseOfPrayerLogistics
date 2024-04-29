package com.spiritcoder.musalalogistics.commons.entity;

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
public class Property extends AbstractAuditable{
    @Id
    @GeneratedValue
    private Integer id;
    private String property;
    private String state;
}
