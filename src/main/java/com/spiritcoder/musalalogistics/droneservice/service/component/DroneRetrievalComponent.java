package com.spiritcoder.musalalogistics.droneservice.service.component;

import com.spiritcoder.musalalogistics.commons.cache.CacheFactory;
import com.spiritcoder.musalalogistics.commons.cache.CacheManager;
import com.spiritcoder.musalalogistics.commons.cache.CacheTypeEnum;
import com.spiritcoder.musalalogistics.commons.cache.CacheUtil;
import com.spiritcoder.musalalogistics.commons.config.AppConstants;
import com.spiritcoder.musalalogistics.droneservice.api.DroneResponse;
import com.spiritcoder.musalalogistics.droneservice.dto.DroneDTO;
import com.spiritcoder.musalalogistics.droneservice.dto.DroneRecord;
import com.spiritcoder.musalalogistics.droneservice.entity.Drone;
import com.spiritcoder.musalalogistics.droneservice.entity.DroneMetadata;
import com.spiritcoder.musalalogistics.droneservice.enums.StateEnum;
import com.spiritcoder.musalalogistics.droneservice.medication.entity.Medication;
import com.spiritcoder.musalalogistics.droneservice.medication.repository.MedicationManager;
import com.spiritcoder.musalalogistics.droneservice.repository.DroneManager;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class DroneRetrievalComponent {

    private static final Logger LOG = LoggerFactory.getLogger(DroneRetrievalComponent.class);

    private final CacheFactory cacheFactory;

    private final MedicationManager medicationManager;

    private final DroneManager droneManager;

    private CacheManager cacheManager;



    public DroneResponse getLoadedDroneItems(int droneId){

        DroneResponse droneResponse = new DroneResponse();

        try{
            cacheManager = cacheFactory.getCacheManager(CacheTypeEnum.REDIS);

            if(cacheManager.isExists(AppConstants.DRONE_CACHE)){

                droneResponse = getLoadedItemsFromCache(cacheManager, droneId);

            }else {

                droneResponse = getLoadedItemsFromRepository(droneId);
            }
        }catch(Exception exception){

            LOG.error(exception.getMessage());
            droneResponse = getLoadedItemsFromRepository(droneId);
        }

        return droneResponse;
    }

    private DroneResponse getLoadedItemsFromRepository(int droneId) {

        DroneResponse droneResponse = new DroneResponse();
        Optional<List<Medication>> itemsForDrone = medicationManager.getLoadedItems(droneId);

        if(itemsForDrone.isEmpty()){
            droneResponse = buldUnavailableItemsDroneResponse();
        }else{
            droneResponse = buildResponseWithAvailableItems(itemsForDrone.get(), droneId);
        }

       return droneResponse;
    }

    /**
     *  assumption: the items list in the cache will only be emptied when state is IDLE - the items
     *   will remain the list throughout the remaining states (LOADED, DELIVERING, DELIVERED AND RETURNING ) of the delivery cycle
     *
     * @param cacheManager
     * @param droneId
     * @return
     */
    private DroneResponse getLoadedItemsFromCache(CacheManager cacheManager, int droneId) {

        DroneMetadata cachedDrone = null;

        String droneKey = CacheUtil.generateKey(String.valueOf(droneId));
        Optional<Object> cachedDroneObject = cacheManager.get(droneKey, AppConstants.DRONE_CACHE);

        if(cachedDroneObject.isPresent()){
            cachedDrone = (DroneMetadata) cachedDroneObject.get();
        }

        assert cachedDrone != null;

        if(cachedDrone.getCurrentBatchList().isEmpty() || cachedDrone.getCurrentState().equals(StateEnum.IDLE)){
            return buldUnavailableItemsDroneResponse();
        }

        return buildResponseWithAvailableItems(cachedDrone.getCurrentBatchList(), cachedDrone.getId());
    }




    public DroneResponse getLoadableDrones(){

        Optional<List<Drone>> loadableDrones =  droneManager.findAllLoadableDrones();

        List<DroneRecord> droneList2 =  new ArrayList<>();

        if(!loadableDrones.isEmpty()){
            List<Drone> droneList =  loadableDrones.get();

            droneList.forEach(drone -> {
                DroneRecord record = new DroneRecord(drone.getId(), drone.getSerial(), drone.getModel(), drone.getWeight());
                droneList2.add(record);
            });

            return buildResponseWithAvailableItems(droneList2);
        }

        return buildResponseWithoutAvailableItems(droneList2);
    }


    public DroneResponse getBatteryLevel(int droneId){
        DroneResponse droneResponse = new DroneResponse();

        try{
            cacheManager = cacheFactory.getCacheManager(CacheTypeEnum.REDIS);

            if(cacheManager.isExists(AppConstants.DRONE_CACHE)){

                droneResponse = getBatteryLevelFromCache(cacheManager, droneId);

            }else {

                droneResponse = getBatteryLevelFromRepository(droneId);
            }
        }catch(Exception exception){

            LOG.error(exception.getMessage());
            droneResponse = getBatteryLevelFromRepository(droneId);
        }

        return droneResponse;
    }

    private DroneResponse getBatteryLevelFromCache(CacheManager cacheManager, int droneId) {
        DroneMetadata cachedDrone = null;

        String droneKey = CacheUtil.generateKey(String.valueOf(droneId));
        Optional<Object> cachedDroneObject = cacheManager.get(droneKey, AppConstants.DRONE_CACHE);

        if(cachedDroneObject.isPresent()){
            cachedDrone = (DroneMetadata) cachedDroneObject.get();
        }

        assert cachedDrone != null;
        byte batteryLevel = cachedDrone.getCurrentBatteryLevel();

        return buildAvailableBatteryLevel(batteryLevel);
    }

    private DroneResponse buildAvailableBatteryLevel(byte batteryLevel) {
        DroneDTO droneDTO = new DroneDTO();
        droneDTO.setBatteryLevel(batteryLevel);
        return buildDroneResponse(null, AppConstants.SUCCESS_MESSAGE, droneDTO);
    }

    private DroneResponse getBatteryLevelFromRepository(int droneId) {
        byte batteryLevel =  droneManager.getBatteryLevel(droneId);
        return buildResponseWithBatteryDetails(batteryLevel);
    }

    private DroneResponse buildResponseWithBatteryDetails(byte batteryLevel){
        DroneDTO droneDTO = new DroneDTO();
        droneDTO.setBatteryLevel(batteryLevel);
        return buildDroneResponse(null, AppConstants.SUCCESS_MESSAGE, droneDTO);
    }

    private DroneResponse buildResponseWithAvailableItems(List<Medication> currentBatchList, int droneId) {
        return buildDroneResponse(null, AppConstants.SUCCESS_MESSAGE, buildDroneDTO(droneId, currentBatchList));
    }

    private DroneResponse buildResponseWithAvailableItems(List<DroneRecord> droneRecords) {
        return buildDroneResponse(null, AppConstants.SUCCESS_MESSAGE, buildDroneDTO(droneRecords));
    }

    private DroneResponse buldUnavailableItemsDroneResponse() {
        return buildDroneResponse(AppConstants.DRONE_IN_IDLE_STATE, AppConstants.SUCCESS_MESSAGE, null);
    }

    private DroneResponse buildResponseWithoutAvailableItems(List<DroneRecord> droneList2) {
        return buildDroneResponse(AppConstants.NO_DRONE_IN_IDLE_STATE, AppConstants.SUCCESS_MESSAGE, null);
    }

    public DroneResponse buildDroneResponse(String errorMessage, String message, DroneDTO droneDTO){
        return DroneResponse
                .builder()
                .message(message)
                .droneDTO(droneDTO)
                .errors( getErrorArray(errorMessage) )
                .build();
    }

    private ArrayList<String> getErrorArray(String errorMessage){
        ArrayList<String> errors = new ArrayList<>();
        errors.add(errorMessage);
        return  errors;
    }

    private DroneDTO buildDroneDTO(List<DroneRecord> droneList2) {
        DroneDTO droneDTO = new DroneDTO();
        droneDTO.setId(0);
        droneDTO.setLoadedItems(null);
        droneDTO.setDroneRecords(droneList2);
        return droneDTO;
    }

    private DroneDTO buildDroneDTO(int id, List<Medication> items){
        if(id > 0 && !items.isEmpty()){
            DroneDTO droneDTO = new DroneDTO();
            droneDTO.setId(id);
            droneDTO.setLoadedItems(items);
            return droneDTO;
        }
        return null;
    }
}
