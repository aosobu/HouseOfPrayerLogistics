package com.spiritcoder.musalalogistics.workers.batchjobs;


import com.spiritcoder.musalalogistics.commons.config.AppConstants;
import com.spiritcoder.musalalogistics.commons.entity.Property;
import com.spiritcoder.musalalogistics.commons.exception.MusalaLogisticsException;
import com.spiritcoder.musalalogistics.commons.repository.PropertyManager;
import com.spiritcoder.musalalogistics.droneservice.entity.Drone;
import com.spiritcoder.musalalogistics.droneservice.enums.StateEnum;
import com.spiritcoder.musalalogistics.droneservice.repository.DroneManager;
import jakarta.annotation.PreDestroy;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;


@Component
@RequiredArgsConstructor
public class DroneOnboardingJob implements Job {

    private static final Logger LOG = LoggerFactory.getLogger(DroneOnboardingJob.class);

    private final DroneManager droneManager;

    private final PropertyManager propertyManager;

    private Property property;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        boolean lockProcess = false;

        try{

            if(!isDroneOnboardingProcessEnabled()) return; //prevents race condition
            
            lockProcess = lockDroneOnboardingProcess(property);
            
            Optional<List<Drone>> inactiveDrones  = droneManager.getAllInActiveDrones();

            inactiveDrones.ifPresent(this::onboardNewDrones);

            unlockDroneOnboardingProcess(property);

        }catch(MusalaLogisticsException musalaLogisticsException){

            throw new MusalaLogisticsException(musalaLogisticsException.getMessage());

        }finally {
            if(lockProcess){
                unlockDroneOnboardingProcess(property);
            }
        }

    }

    private void unlockDroneOnboardingProcess(Property property) {
        try{

            propertyManager.updateProperty("true", property.getId());

        }catch(MusalaLogisticsException musalaLogisticsException){
            //TODO:: log to database
            LOG.error(musalaLogisticsException.getMessage(), musalaLogisticsException.getCause());
            throw new MusalaLogisticsException(musalaLogisticsException.getMessage(), musalaLogisticsException.getCause());
        }
    }

    @Transactional
    private void onboardNewDrones(List<Drone> inactiveDrones) {
        String message;

        try{

                message = String.format("%s about to register %d new drones ", DroneOnboardingJob.class.getSimpleName(), inactiveDrones.size());
                LOG.info(message);

                AtomicReference<String> atomicReference = new AtomicReference<>();

                inactiveDrones.forEach(drone -> {

                    boolean insertDroneStateSnapshotStatus = droneManager.insertDroneActivitySnapshot(StateEnum.IDLE, drone.getId());
                    boolean insertDroneBatterySnapshotStatus = droneManager.insertDroneBatterySnapshotRecord(drone.getId(), generateBatteryLevel());
                    boolean updateActivationState = droneManager.updateActivationStatus(true, drone.getId());

                    boolean isCacheUpdateSuccessful = addDroneMetadataToCache(insertDroneStateSnapshotStatus, updateActivationState, insertDroneBatterySnapshotStatus);

                    publishMessage(insertDroneStateSnapshotStatus, updateActivationState, insertDroneBatterySnapshotStatus,isCacheUpdateSuccessful, atomicReference, drone);
                });

        }catch(MusalaLogisticsException musalaLogisticsException){

            LOG.error(musalaLogisticsException.getMessage(), musalaLogisticsException.getCause());
            throw new MusalaLogisticsException(musalaLogisticsException.getMessage(), musalaLogisticsException.getCause());
        }
    }

    private boolean addDroneMetadataToCache(boolean insertDroneStateSnapshotStatus, boolean updateActivationState, boolean insertDroneBatterySnapshotStatus) {

        // ping cache for availability

        // if cache available then update else update

        return false;
    }

    private byte generateBatteryLevel() {
        return (byte) (Math.random() * 101);
    }

    private void publishMessage(boolean insertDroneSnapshotStatus, boolean updateActivationState, boolean insertBatterySnapshotStatus,
                                boolean isCacheUpdateSuccessful, AtomicReference<String> atomicReference, Drone drone){

        if(insertDroneSnapshotStatus && updateActivationState && insertBatterySnapshotStatus){
            atomicReference.set(String.format(" Drone with id %d and serial number %s successfully activated", drone.getId(), drone.getSerial()));
            LOG.info(atomicReference.get());
        }

        if(isCacheUpdateSuccessful){
            atomicReference.set(String.format(" Drone with id %d and serial number %s metadata successfully added to cache", drone.getId(), drone.getSerial()));
            LOG.info(atomicReference.get());
        }
    }

    private boolean lockDroneOnboardingProcess(Property property) {

        try{

            propertyManager.updateProperty("false", property.getId());
            return true;

        }catch(MusalaLogisticsException musalaLogisticsException){

            LOG.error(musalaLogisticsException.getMessage(), musalaLogisticsException.getCause());
            throw new MusalaLogisticsException(musalaLogisticsException.getMessage(), musalaLogisticsException.getCause());
        }
    }

    /**
     * This method performs a side effect on the property field
     * @ return
     */
    private boolean isDroneOnboardingProcessEnabled() {
        Optional<Property> property;

        try{

            property = propertyManager.getPropertyByKey(AppConstants.DRONE_NEW_ACTIVATION_PROCESS_ENABLED);
            property.orElseThrow();
            this.property = property.get();

        }catch (MusalaLogisticsException musalaLogisticsException){

            LOG.error(musalaLogisticsException.getMessage(), musalaLogisticsException.getCause());
            throw new MusalaLogisticsException(musalaLogisticsException.getMessage(), musalaLogisticsException.getCause());
        }

        return !property.get().getState().equals("false");
    }

    @PreDestroy
    public void destroy(){
        LOG.info(String.format("%s instance executing via %s is being destroyed", DroneOnboardingJob.class.getSimpleName(), Thread.currentThread().getName()));
    }

}
