package com.spiritcoder.musalalogistics.droneservice.service;

import com.spiritcoder.musalalogistics.droneservice.api.DroneRequest;
import com.spiritcoder.musalalogistics.droneservice.api.DroneResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DroneService {

    private final DroneOperationsFacade droneOperationsFacade;

    public DroneResponse registerDrone(DroneRequest droneRequest){
        return droneOperationsFacade.registerDrone(droneRequest);
    }

    public DroneResponse loadDrone(int droneId){
        return droneOperationsFacade.loadDrone(droneId);
    }
}
