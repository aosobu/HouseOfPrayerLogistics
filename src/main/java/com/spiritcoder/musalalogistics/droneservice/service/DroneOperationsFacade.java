package com.spiritcoder.musalalogistics.droneservice.service;

import com.spiritcoder.musalalogistics.droneservice.api.DroneRequest;
import com.spiritcoder.musalalogistics.droneservice.api.DroneResponse;
import com.spiritcoder.musalalogistics.droneservice.dto.DroneRecord;
import com.spiritcoder.musalalogistics.droneservice.service.component.DroneRetrievalComponent;
import com.spiritcoder.musalalogistics.droneservice.service.component.DroneRegisterComponent;
import com.spiritcoder.musalalogistics.droneservice.service.component.LoadDroneViaControllerComponent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DroneOperationsFacade {

    private final DroneRegisterComponent droneRegisterComponent;

    private final LoadDroneViaControllerComponent loadDrone;

    private final DroneRetrievalComponent droneRetrievalComponent;

    public DroneResponse registerDrone(DroneRequest droneRequest){
        return droneRegisterComponent.registerDrone(droneRequest);
    }

    public DroneResponse loadDrone(int droneId){
        return loadDrone.loadDroneViaApi(droneId);
    }

    public DroneResponse getLoadedItems(int droneId) {
        return droneRetrievalComponent.getLoadedDroneItems(droneId);
    }

    public List<DroneRecord> getLoadableDrones() {
        return droneRetrievalComponent.getLoadableDrones();
    }
}