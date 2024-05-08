package com.spiritcoder.musalalogistics.droneservice.service.component;

import com.spiritcoder.musalalogistics.commons.cache.CacheFactory;
import com.spiritcoder.musalalogistics.commons.cache.CacheManager;
import com.spiritcoder.musalalogistics.commons.cache.CacheTypeEnum;
import com.spiritcoder.musalalogistics.commons.exception.MusalaLogisticsException;
import com.spiritcoder.musalalogistics.droneservice.api.DroneRequest;
import com.spiritcoder.musalalogistics.droneservice.api.DroneResponse;
import com.spiritcoder.musalalogistics.droneservice.entity.Drone;
import com.spiritcoder.musalalogistics.droneservice.entity.DroneMetadata;
import com.spiritcoder.musalalogistics.droneservice.enums.StateEnum;
import com.spiritcoder.musalalogistics.droneservice.repository.DroneManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class DroneRegisterComponent {

    private static final Logger LOG = LoggerFactory.getLogger(DroneRegisterComponent.class);

    private final CacheFactory cacheFactory;

    private final DroneManager droneManager;

    @Transactional
    public DroneResponse onboardDrone(DroneRequest droneRequest){



        try{

            boolean insertDrone = droneManager.insertDrone(droneRequest.getSerialNumber(), droneRequest.getModel(), droneRequest.getWeight());
            Optional<Drone> savedDrone = droneManager.findDroneBySerialNumber(droneRequest.getSerialNumber());

            savedDrone.ifPresent(drone -> {

                boolean isInsertDroneActivitySnapshot = droneManager.insertDroneActivitySnapshot(StateEnum.IDLE, drone.getId());

                boolean isInsertDroneBatterySnapshot = droneManager.insertDroneBatterySnapshotRecord(drone.getId(), droneRequest.getBattery());

                boolean  isInsertDroneBattery = droneManager.insertDroneBattery(drone.getId(), droneRequest.getBattery());

                boolean isInsertDroneActivity = true;

                boolean isUpdateDroneActivationState = droneManager.updateActivationStatus(true, drone.getId());

                boolean isCacheUpdated = addDroneMetadataToCache(buildDroneCachableEntity(droneRequest));

                publishMessage(isInsertDroneActivitySnapshot, isInsertDroneBatterySnapshot, isInsertDroneBattery, isInsertDroneActivity,
                                                isUpdateDroneActivationState, isCacheUpdated, drone);

            });

        }catch(MusalaLogisticsException musalaLogisticsException) {
            //TODO:: log into error reporting table in database
           throw new MusalaLogisticsException(musalaLogisticsException.getMessage(), musalaLogisticsException.getCause());
        }

        return new DroneResponse();
    }

    private DroneMetadata buildDroneCachableEntity(DroneRequest droneRequest) {

        return DroneMetadata
                .builder()
                .serialNumber(droneRequest.getSerialNumber())
                .model(droneRequest.getModel().toString())
                .weight(droneRequest.getWeight())
                .activated(true)
                .lastBatchId(null)
                .currentBatchId(null)
                .currentBatchList(new ArrayList<>())
                .currentBatteryLevel(droneRequest.getBattery())
                .lastBatteryLevel(droneRequest.getBattery())
                .currentState(StateEnum.IDLE)
                .build();

    }

    private boolean addDroneMetadataToCache(DroneMetadata droneMetadata) {

        CacheManager cacheManager = cacheFactory.getCacheManager(CacheTypeEnum.SPRING_CACHE);

        if(cacheManager.getCacheNames().contains("drone")){
            // generate key
            // insert data using generated key
            return true;
        }
        // add data to cache
        return false;
    }

    private void publishMessage(boolean isInsertDroneActivitySnapshot, boolean isInsertDroneBatterySnapshot, boolean isInsertDroneBattery,
                                boolean isInsertDroneActivity, boolean isUpdateDroneActivationState, boolean isCacheUpdated,  Drone drone){

        if(isInsertDroneActivitySnapshot && isInsertDroneBatterySnapshot && isInsertDroneBattery && isInsertDroneActivity && isUpdateDroneActivationState){
            String message =  String.format(" Drone with id %d and serial number %s successfully activated", drone.getId(), drone.getSerial());
            LOG.info(message);
        }

        if(isCacheUpdated){
            String message =  String.format(" Drone with id %d and serial number %s successfully cached", drone.getId(), drone.getSerial());
            LOG.info(message);
        }else{
            String message =  String.format(" Drone with id %d and serial number %s was not successfully cached. send email to administrator", drone.getId(), drone.getSerial());
            //TODO:: add to error reporting table as critical
            LOG.info(message);
        }
    }
}
