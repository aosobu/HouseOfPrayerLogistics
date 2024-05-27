package com.spiritcoder.musalalogistics.droneservice.api;

import com.spiritcoder.musalalogistics.droneservice.enums.ModelEnum;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class DroneRequest {

    @Pattern(regexp = "^.{1,100}$", message = "serial number cannot exceed 100 characters")
    @NotBlank
    private String serialNumber;

    @NotNull(message = "model cannot be null or empty")
    private ModelEnum model;

    @Min(value = 200, message = "weight should not be less than 200")
    @Max(value = 500, message = "weight should not be greater than 500")
    @NotNull(message = "weight cannot be null or empty")
    private Short weight;

    @Min(value = 0, message = "battery level should not be less than 0")
    @Max(value = 100, message = "battery level should not be greater than 100")
    @NotNull(message = "battery level cannot be null or empty")
    private Byte battery;
}
