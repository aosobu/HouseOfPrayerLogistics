package com.spiritcoder.musalalogistics.droneservice.api;

import com.spiritcoder.musalalogistics.droneservice.enums.ModelEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class DroneRequest {

    @Pattern(regexp = "")
    @NotBlank
    private String serialNumber;

    private ModelEnum model;

    @NotBlank
    @Size(min=200, max=500)
    private Short weight;

    @NotBlank
    @Size(max=100)
    private Byte battery;
}
