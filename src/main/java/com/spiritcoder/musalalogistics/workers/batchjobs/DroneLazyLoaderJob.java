package com.spiritcoder.musalalogistics.workers.batchjobs;

import com.spiritcoder.musalalogistics.commons.config.AppConstants;
import com.spiritcoder.musalalogistics.commons.entity.Property;
import com.spiritcoder.musalalogistics.commons.exception.MusalaLogisticsException;
import com.spiritcoder.musalalogistics.commons.repository.PropertyManager;
import com.spiritcoder.musalalogistics.droneservice.entity.Drone;
import com.spiritcoder.musalalogistics.droneservice.entity.DroneActivitySnapshot;
import com.spiritcoder.musalalogistics.droneservice.enums.StateEnum;
import com.spiritcoder.musalalogistics.droneservice.medication.entity.Medication;
import com.spiritcoder.musalalogistics.droneservice.medication.repository.MedicationManager;
import com.spiritcoder.musalalogistics.droneservice.repository.DroneManager;
import com.spiritcoder.musalalogistics.droneservice.util.DroneLoader;
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
public class DroneLazyLoaderJob implements Job {

    private static final Logger LOG = LoggerFactory.getLogger(DroneLazyLoaderJob.class);

    private final DroneManager droneManager;

    private final MedicationManager medicationManager;

    private final PropertyManager propertyManager;

    private final DroneLoader droneLoader;

    private Property property;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {

        boolean processLock = false;

        try{

            if(!isDroneLoadingProcessingEnabled()) return; //prevents race condition

            processLock = lockDroneLoadingProcess(property);

            Optional<List<DroneActivitySnapshot>> loadableDrones = droneManager.findAllLoadableDrones();

            loadableDrones.ifPresent(this::loadDrone);

            unlockDroneOnboardingProcess(property);

        }catch(MusalaLogisticsException musalaLogisticsException){

            //TODO:: log error on database
            throw new MusalaLogisticsException(musalaLogisticsException.getMessage());

        }finally{
            if(processLock){
                unlockDroneOnboardingProcess(property);
            }
        }
    }

    @Transactional
    private void loadDrone(List<DroneActivitySnapshot> loadableDrones) {
        List<Medication> loadedItems;
        AtomicReference<List<Medication>> medicationReference = new AtomicReference<>();

        try{

            loadableDrones.forEach( drone -> {

                boolean updateDroneState = droneManager.updateDroneStateSnapshot(StateEnum.LOADING.name(), drone.getId());
                List<Medication> dispatchAbleMedicationItems = medicationManager.findAllLoadableMedication();

                Drone droneToBeLoaded = droneManager.getDroneById(drone.getId());
                if(droneToBeLoaded != null) {
                    medicationReference.set(droneLoader.loadDrone(dispatchAbleMedicationItems, droneToBeLoaded));
                }

                // if load is successful
                //

            });

        }catch(MusalaLogisticsException musalaLogisticsException){

            LOG.error(musalaLogisticsException.getMessage(), musalaLogisticsException.getCause());
            throw new MusalaLogisticsException(musalaLogisticsException.getMessage(), musalaLogisticsException.getCause());
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

    private boolean lockDroneLoadingProcess(Property property) {

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
    private boolean isDroneLoadingProcessingEnabled() {
        Optional<Property> property;

        try{

            property = propertyManager.getPropertyByKey(AppConstants.DRONE_LOADING_PROCESS_ENABLED);
            property.orElseThrow();
            this.property = property.get();

        }catch (MusalaLogisticsException musalaLogisticsException){

            LOG.error(musalaLogisticsException.getMessage(), musalaLogisticsException.getCause());
            throw new MusalaLogisticsException(musalaLogisticsException.getMessage(), musalaLogisticsException.getCause());
        }

        return !property.get().getState().equals("false");
    }
}
