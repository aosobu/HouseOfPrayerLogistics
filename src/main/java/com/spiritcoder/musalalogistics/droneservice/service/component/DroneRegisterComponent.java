package com.spiritcoder.musalalogistics.droneservice.service.component;

import com.spiritcoder.musalalogistics.commons.cache.CacheFactory;
import com.spiritcoder.musalalogistics.commons.cache.CacheManager;
import com.spiritcoder.musalalogistics.commons.cache.CacheTypeEnum;
import com.spiritcoder.musalalogistics.commons.cache.CacheUtil;
import com.spiritcoder.musalalogistics.commons.config.AppConstants;
import com.spiritcoder.musalalogistics.droneservice.api.DroneRequest;
import com.spiritcoder.musalalogistics.droneservice.api.DroneResponse;
import com.spiritcoder.musalalogistics.droneservice.dto.DroneDTO;
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

    private int droneId;


    @Transactional
    public DroneResponse registerDrone(DroneRequest droneRequest){

        try{

            boolean isDuplicateRegistration = checkDuplicateRegistration(droneRequest);

            if(isDuplicateRegistration){
                return buildDuplicateRegistrationDroneResponse();
            }

            boolean isOnboarded = onboardDrone(droneRequest);

            if(isOnboarded){
                addDroneMetadataToCache(buildDroneCachableEntity(droneRequest));
                return buildSuccessRegistrationDroneResponse();
            }

        }catch(Exception ex){
            //TODO:: add exception to exception table
            LOG.error(ex.getMessage(), ex.getCause());
        }

        return buildFailureRegistrationDroneResponse();
    }

    private boolean checkDuplicateRegistration(DroneRequest droneRequest) {
        Optional<Drone> duplicateDroneRegistration = droneManager.findDroneBySerialNumber(droneRequest.getSerialNumber());
        return duplicateDroneRegistration.isPresent();
    }

    /**
     * performs side effect on droneId
     * @param droneRequest
     * @return
     */
    @Transactional
    public boolean onboardDrone(DroneRequest droneRequest){
        try{

            boolean insertDrone = droneManager.insertDrone(droneRequest.getSerialNumber(), droneRequest.getModel(), droneRequest.getWeight());

            if(insertDrone) {

                Optional<Drone> savedDrone = droneManager.findDroneBySerialNumber(droneRequest.getSerialNumber());

                savedDrone.ifPresent(drone -> {

                    this.droneId = drone.getId();

                    boolean isInsertDroneActivitySnapshot = droneManager.insertDroneActivitySnapshot(StateEnum.IDLE, drone.getId());

                    boolean isInsertDroneBatterySnapshot = droneManager.insertDroneBatterySnapshotRecord(drone.getId(), droneRequest.getBattery());

                    boolean  isInsertDroneBattery = droneManager.insertDroneBattery(drone.getId(), droneRequest.getBattery());

                    boolean isInsertDroneActivity = droneManager.insertDroneActivity(drone.getId(), StateEnum.IDLE.toString(), null);

                    boolean isUpdateDroneActivationState = droneManager.updateActivationStatus(true, drone.getId());

                    publishMessage(isInsertDroneActivitySnapshot, isInsertDroneBatterySnapshot, isInsertDroneBattery, isInsertDroneActivity,
                            isUpdateDroneActivationState, drone);

                });
            }

        }catch(Exception ex) {
            return false;
        }

        return true;
    }



    private DroneMetadata buildDroneCachableEntity(DroneRequest droneRequest) {

        return DroneMetadata
                .builder()
                .id(droneId)
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

        if(cacheManager.getCacheNames().contains(AppConstants.DRONE_CACHE)){
            String key = CacheUtil.generateKey(String.valueOf(droneMetadata.getId()));
            cacheManager.insert(key, droneMetadata, AppConstants.DRONE_CACHE);
            return true;
        }

        return false;
    }

    private void publishMessage(boolean isInsertDroneActivitySnapshot, boolean isInsertDroneBatterySnapshot, boolean isInsertDroneBattery,
                                boolean isInsertDroneActivity, boolean isUpdateDroneActivationState,  Drone drone){

        if(isInsertDroneActivitySnapshot && isInsertDroneBatterySnapshot && isInsertDroneBattery && isInsertDroneActivity && isUpdateDroneActivationState){
            String message =  String.format(" Drone with id %d and serial number %s successfully activated", drone.getId(), drone.getSerial());
            LOG.info(message);
        }

    }

    private DroneResponse buildSuccessRegistrationDroneResponse() {
        return DroneResponse
                .builder()
                .message(AppConstants.SUCCESS_MESSAGE)
                .droneDTO(new DroneDTO(droneId))
                .errors(new ArrayList<>())
                .build();
    }

    private DroneResponse buildFailureRegistrationDroneResponse() {
        return buildDroneResponse( AppConstants.FAILURE_REGISTRATION, AppConstants.SUCCESS_MESSAGE);
    }

    private DroneResponse buildDuplicateRegistrationDroneResponse() {
        return buildDroneResponse( AppConstants.DUPLICATE_REGISTRATION, AppConstants.SUCCESS_MESSAGE);
    }

    public DroneResponse buildDroneResponse(String errorMessage, String message){
        return DroneResponse
                .builder()
                .message(message)
                .droneDTO(null)
                .errors( getErrorArray(errorMessage) )
                .build();
    }

    private ArrayList<String> getErrorArray(String errorMessage){
        ArrayList<String> errors = new ArrayList<>();
        errors.add(errorMessage);
        return  errors;
    }
}
