package com.spiritcoder.musalalogistics.droneservice.service;

import com.spiritcoder.musalalogistics.droneservice.api.DroneRequest;
import com.spiritcoder.musalalogistics.droneservice.api.DroneResponse;
import com.spiritcoder.musalalogistics.droneservice.service.component.DroneRetrievalComponent;
import com.spiritcoder.musalalogistics.droneservice.service.component.DroneRegisterComponent;
import com.spiritcoder.musalalogistics.droneservice.service.component.DroneEagerLoaderComponent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DroneOperationsFacade {

    private final DroneRegisterComponent droneRegisterComponent;

    private final DroneEagerLoaderComponent loadDrone;

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

    public DroneResponse getLoadableDrones() {
        return droneRetrievalComponent.getLoadableDrones();
    }

    public DroneResponse getBatteryLevel(int droneId) {
        return droneRetrievalComponent.getBatteryLevel(droneId);
    }
}