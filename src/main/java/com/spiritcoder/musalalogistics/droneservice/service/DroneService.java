package com.spiritcoder.musalalogistics.droneservice.service;

import com.spiritcoder.musalalogistics.droneservice.api.DroneRequest;
import com.spiritcoder.musalalogistics.droneservice.api.DroneResponse;
import com.spiritcoder.musalalogistics.droneservice.dto.DroneRecord;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DroneService {

    private final DroneOperationsFacade droneOperations;

    public DroneResponse registerDrone(DroneRequest droneRequest){
        return droneOperations.registerDrone(droneRequest);
    }

    public DroneResponse loadDrone(int droneId){
        return droneOperations.loadDrone(droneId);
    }

    public DroneResponse getLoadedItems(int droneId) {
        return droneOperations.getLoadedItems(droneId);
    }

    public List<DroneRecord> getLoadableDrones() {
        return droneOperations.getLoadableDrones();
    }
}
