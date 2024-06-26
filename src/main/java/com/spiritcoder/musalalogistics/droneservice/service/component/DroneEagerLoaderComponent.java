package com.spiritcoder.musalalogistics.droneservice.service.component;

import com.spiritcoder.musalalogistics.commons.cache.CacheFactory;
import com.spiritcoder.musalalogistics.commons.cache.CacheManager;
import com.spiritcoder.musalalogistics.commons.cache.CacheTypeEnum;
import com.spiritcoder.musalalogistics.commons.cache.CacheUtil;
import com.spiritcoder.musalalogistics.commons.config.AppConstants;
import com.spiritcoder.musalalogistics.droneservice.api.DroneResponse;
import com.spiritcoder.musalalogistics.droneservice.entity.Drone;
import com.spiritcoder.musalalogistics.droneservice.entity.DroneMetadata;
import com.spiritcoder.musalalogistics.droneservice.enums.LoadTypeEnum;
import com.spiritcoder.musalalogistics.droneservice.enums.StateEnum;
import com.spiritcoder.musalalogistics.droneservice.repository.DroneManager;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class DroneEagerLoaderComponent {

    private static final Logger LOG = LoggerFactory.getLogger(DroneRegisterComponent.class);

    private final CacheFactory cacheFactory;

    private final DroneLoaderComponent droneLoader;

    private final DroneManager droneManager;

    private CacheManager cacheManager;


    public DroneResponse loadDroneViaApi(int droneId){

        DroneResponse droneResponse = new DroneResponse();

        try{

            cacheManager = cacheFactory.getCacheManager(CacheTypeEnum.REDIS);

            if(cacheManager.isCacheAvailable(AppConstants.DRONE_CACHE)){

                droneResponse = loadDroneUsingCacheDetails(droneId);

            }else {

                droneResponse = loadDroneUsingRepositoryDetails(droneId);
            }

        }catch(Exception exception){
            LOG.error(exception.getMessage());
            droneResponse = loadDroneUsingRepositoryDetails(droneId);
        }

        return droneResponse;
    }

    private DroneResponse loadDroneUsingRepositoryDetails(int droneId) {

        DroneResponse droneResponse;
        Optional<Drone> droneForLoading = droneManager.checkIfDroneIsLoadable(droneId);

        if(droneForLoading.isPresent()){
            droneResponse = loadDrone(droneId);
            return droneResponse;
        }

        return buldFailedDroneResponse();
    }

    private DroneResponse loadDroneUsingCacheDetails(int droneId) {

        boolean isLoadedStateInCache = checkCacheForLoadState(droneId);

        if(isLoadedStateInCache){

            return buldFailedDroneResponse();

        }else{

            return checkStateInCacheAndLoadDrone(droneId);

        }
    }

    private DroneResponse checkStateInCacheAndLoadDrone(int droneId) {
        if( checkCacheForIdleState(droneId)){
            return loadDrone(droneId);
        }
        return buldFailedDroneResponse();
    }

    private DroneResponse loadDrone(int droneId) {
        boolean loadStatus = droneLoader.loadDrone(droneId, LoadTypeEnum.EAGER);
        if(loadStatus){
            return buildSuccessDroneResponse();
        }
        return buldFailedDroneResponse();
    }




    private boolean checkCacheForIdleState(int droneId) {
        DroneMetadata cachedDrone = null;

        String droneKey = CacheUtil.generateKey(String.valueOf(droneId));
        Optional<Object> cachedDroneObject = cacheManager.get(droneKey, AppConstants.DRONE_CACHE);

        if(cachedDroneObject.isPresent()){
            cachedDrone = (DroneMetadata) cachedDroneObject.get();
        }

        assert cachedDrone != null;
        return cachedDrone.getCurrentBatchList().isEmpty() && cachedDrone.getCurrentState().equals(StateEnum.IDLE);
    }

    private boolean checkCacheForLoadState(int droneId) {
        DroneMetadata cachedDrone = null;

        String droneKey = CacheUtil.generateKey(String.valueOf(droneId));
        Optional<Object> cachedDroneObject = cacheManager.get(droneKey, AppConstants.DRONE_CACHE);

        if(cachedDroneObject.isPresent()){
            cachedDrone = (DroneMetadata) cachedDroneObject.get();
        }

        assert cachedDrone != null;
        return !cachedDrone.getCurrentBatchList().isEmpty() && cachedDrone.getCurrentState().equals(StateEnum.LOADED);
    }

    private DroneResponse buldFailedDroneResponse() {
        return buildDroneResponse(AppConstants.DRONE_UNAVAILABLE_FOR_LOADING, AppConstants.SUCCESS_MESSAGE);
    }

    private DroneResponse buildSuccessDroneResponse() {
        return buildDroneResponse(null, AppConstants.DRONE_LOAD_SUCCESS_MESSAGE);
    }

    private DroneResponse buildDroneResponse(String errorMessage, String message){
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
