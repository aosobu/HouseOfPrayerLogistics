package com.spiritcoder.musalalogistics.droneservice.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.spiritcoder.musalalogistics.droneservice.dto.DroneDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DroneResponse {

    private String message;

    @JsonProperty("drone")
    private DroneDTO droneDTO;

    private List<String> errors = new ArrayList<>();

    public boolean isEmptyErrorList(){
        return this.getErrors().isEmpty();
    }
}
