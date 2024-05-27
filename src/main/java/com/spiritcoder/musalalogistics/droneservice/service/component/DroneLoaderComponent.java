package com.spiritcoder.musalalogistics.droneservice.service.component;

import com.spiritcoder.musalalogistics.commons.cache.CacheFactory;
import com.spiritcoder.musalalogistics.commons.cache.CacheManager;
import com.spiritcoder.musalalogistics.commons.cache.CacheTypeEnum;
import com.spiritcoder.musalalogistics.commons.cache.CacheUtil;
import com.spiritcoder.musalalogistics.commons.config.AppConstants;
import com.spiritcoder.musalalogistics.commons.entity.Property;
import com.spiritcoder.musalalogistics.commons.repository.PropertyManager;
import com.spiritcoder.musalalogistics.droneservice.api.DroneResponse;
import com.spiritcoder.musalalogistics.droneservice.entity.Drone;
import com.spiritcoder.musalalogistics.droneservice.entity.DroneMedicationBatchSnapshot;
import com.spiritcoder.musalalogistics.droneservice.entity.DroneMetadata;
import com.spiritcoder.musalalogistics.droneservice.enums.LoadTypeEnum;
import com.spiritcoder.musalalogistics.droneservice.enums.StateEnum;
import com.spiritcoder.musalalogistics.droneservice.medication.entity.Medication;
import com.spiritcoder.musalalogistics.droneservice.medication.repository.MedicationManager;
import com.spiritcoder.musalalogistics.droneservice.repository.DroneManager;
import com.spiritcoder.musalalogistics.droneservice.util.DroneLoader;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

@Component
@RequiredArgsConstructor
public class DroneLoaderComponent {

    private static final Logger LOG = LoggerFactory.getLogger(DroneLoaderComponent.class);

    private final DroneManager droneManager;

    private final MedicationManager medicationManager;

    private final DroneLoader droneLoader;

    private final PropertyManager propertyManager;

    private final CacheFactory cacheFactory;

    @Transactional
    public boolean loadDrone(Integer droneId, LoadTypeEnum loadType){

        CacheManager cacheManager = cacheFactory.getCacheManager(CacheTypeEnum.REDIS);
        DroneMetadata cachedDrone = null;

        AtomicReference<List<Medication>> medicationListReference = new AtomicReference<>();
        List<Drone>droneList= new ArrayList<>();
        List<Medication> loadedItems;
        String message;

        try{
            if(isEager(loadType) & isAvailableDroneId(droneId)){
                Drone drone = droneManager.getDroneById(droneId);
                droneList.add(drone);
            }else{
                Optional<List<Drone>> loadableDrones = droneManager.findAllLoadableDrones();
                if(loadableDrones.isPresent()){
                    droneList = loadableDrones.get();
                }
            }

            if(droneList.isEmpty()){
                return false;
            }

            //get last batch id
            Optional<DroneMedicationBatchSnapshot> record = droneManager.getNextBatch();
            int batchId = 0;

            if(record.isPresent()){
                batchId = record.get().getBatch();
            }

            for (Drone drone : droneList) {

                int currentDroneId = drone.getId();
                message = String.format("loading drone with serial number %s and weight %d", drone.getSerial(), drone.getWeight());
                publishEventMessage(message);

                //get cached drone record to retrieve current battery level
                String droneKey = CacheUtil.generateKey(String.valueOf(currentDroneId));
                Optional<Object> cachedDroneObject = cacheManager.get(droneKey, AppConstants.DRONE_CACHE);

                if(cachedDroneObject.isPresent()){
                    cachedDrone = (DroneMetadata) cachedDroneObject.get();
                }

                //TODO:: should backup algorithm to function optimally without cache - in the event of a cache outage
                assert cachedDrone != null;
                if(cachedDrone.getCurrentBatteryLevel() > getAllowedLoadableBatteryLevel()){

                    Optional<List<Medication>> medicationList = medicationManager.findAllLoadableMedication();
                    medicationList.ifPresent(medicationListReference::set);
                    loadedItems = droneLoader.loadDrone(medicationListReference.get(), drone);

                    if(loadedItems.isEmpty()){
                        continue;
                    }

                    int totalWeight = loadedItems.stream().mapToInt(Medication::getWeight).sum();
                    message = String.format("total weight of items loaded for drone with size %s  is %d ::  %s", cachedDrone.getSerialNumber(), cachedDrone.getWeight(), totalWeight);
                    publishEventMessage(message);

                    // get next batch
                    if(record.isPresent()){
                        AtomicInteger nextBatch = new AtomicInteger();
                        batchId = batchId + 1;

                        nextBatch.set(batchId);

                        //update drone medication batch
                        droneManager.insertDroneMedicationBatchRecord(nextBatch.get(), currentDroneId);

                        droneManager.insertDroneActivity(currentDroneId, StateEnum.LOADING.toString(), nextBatch.get());
                        droneManager.updateDroneActivitySnapshot(StateEnum.LOADING.toString(), nextBatch.get(), currentDroneId);

                        //update cache record
                        updateDroneAndReloadCache(cachedDrone,cacheManager,droneKey,StateEnum.LOADING);

                        loadedItems.forEach(item -> medicationManager.updateMedicationRecordWithBatchId(item.getId(), nextBatch.get()));

                        droneManager.updateDroneMedicationBatchSnapshotRecord(nextBatch.get(), currentDroneId);

                        //update cache record
                        updateCacheRecord(cachedDrone, loadedItems, nextBatch.get());
                        updateDroneAndReloadCache(cachedDrone,cacheManager,droneKey,StateEnum.LOADED);

                        droneManager.insertDroneActivity(currentDroneId, StateEnum.LOADED.toString(), nextBatch.get());
                        droneManager.updateDroneActivitySnapshot(StateEnum.LOADED.toString(), nextBatch.get(), currentDroneId);
                    }
                }
            }

        }catch(Exception exception){
            return false;
        }

        return true;
    }

    private void publishEventMessage(String message) {
        LOG.info(message);
    }

    private int getAllowedLoadableBatteryLevel() {
        Optional<Property> property;
        try{

            property = propertyManager.getPropertyByKey(AppConstants.DRONE_LOAD_ALLOWABLE_BATTERY_LEVEL);
            property.orElseThrow();

        }catch (Exception exception){
            LOG.error(exception.getMessage());
            throw new RuntimeException(exception.getMessage());
        }

        return Integer.parseInt(property.get().getState());
    }

    private boolean isEager(LoadTypeEnum loadType){
       return  loadType.equals(LoadTypeEnum.EAGER);
    }

    private boolean isAvailableDroneId(Integer droneId){
        return droneId != null && droneId > 0;
    }

    private void updateCacheRecord(DroneMetadata cachedDrone, List<Medication> loadedItems, int nextBatch){
        try{
            List<Medication> cachedLoadedItems = cachedDrone.getCurrentBatchList();
            cachedLoadedItems.clear();
            cachedLoadedItems.addAll(loadedItems);
            cachedDrone.setCurrentBatchList(cachedLoadedItems);

            Integer currentBatchId = cachedDrone.getCurrentBatchId();
            cachedDrone.setLastBatchId(currentBatchId);
            cachedDrone.setCurrentBatchId(nextBatch);

        }catch (Exception exception) {
            throw new RuntimeException(exception.getMessage());
        }
    }

    public void updateDroneAndReloadCache(DroneMetadata cachedDrone,CacheManager cacheManager, String droneKey, StateEnum stateEnum){
        cachedDrone.setCurrentState(stateEnum);
        cacheManager.evictIfPresent(droneKey, AppConstants.DRONE_CACHE);
        cacheManager.insert(droneKey, cachedDrone, AppConstants.DRONE_CACHE);
    }

    public DroneResponse buildSuccessDroneResponse() {
        return DroneResponse
                .builder()
                .message(AppConstants.SUCCESS_MESSAGE)
                .errors(new ArrayList<>())
                .droneDTO(null)
                .build();
    }
}