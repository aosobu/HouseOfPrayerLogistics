package com.spiritcoder.musalalogistics.job.batchjobs;


import com.spiritcoder.musalalogistics.commons.config.AppConstants;
import com.spiritcoder.musalalogistics.commons.entity.Property;
import com.spiritcoder.musalalogistics.commons.exception.MusalaLogisticsException;
import com.spiritcoder.musalalogistics.commons.repository.PropertyManager;
import com.spiritcoder.musalalogistics.drone.entity.Drone;
import com.spiritcoder.musalalogistics.drone.enums.StateEnum;
import com.spiritcoder.musalalogistics.drone.repository.DroneManager;
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


@Transactional
@Component
@RequiredArgsConstructor
public class ActivateNewlyAddedDronesJob implements Job {

    private static final Logger LOG = LoggerFactory.getLogger(ActivateNewlyAddedDronesJob.class);

    private final DroneManager droneManager;

    private final PropertyManager propertyManager;

    private Property property;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {

        try{

            if(!droneActivationProcessingEnabled()) return; //prevents race condition
            
            lockActivateNewlyAddedDroneProcess(property);
            
            Optional<List<Drone>> inactiveDrones  = droneManager.getAllInActiveDrones();

            activateNewlyAddedDrones(inactiveDrones);

            unlockActivateNewlyAddedDroneProcess(property);

        }catch(MusalaLogisticsException musalaLogisticsException){

            throw new MusalaLogisticsException(musalaLogisticsException.getMessage(), musalaLogisticsException.getCause());
        }

    }

    private void unlockActivateNewlyAddedDroneProcess(Property property) {
        try{
            propertyManager.updateProperty("true", property.getId());

        }catch(MusalaLogisticsException musalaLogisticsException){
            throw new MusalaLogisticsException(musalaLogisticsException.getMessage(), musalaLogisticsException.getCause());
        }
    }

    private void activateNewlyAddedDrones(Optional<List<Drone>> inactiveDrones) {
        String message;

        if(inactiveDrones.isPresent()){

            message = String.format("%s about to register %d new drones ", ActivateNewlyAddedDronesJob.class.getSimpleName(), inactiveDrones.get().size());
            LOG.info(message);

            AtomicReference<String> atomicReference = new AtomicReference<>();

            inactiveDrones.get().forEach(drone -> {

                boolean insertStatus = droneManager.addDroneToDroneStateSnapshot(drone.getId(), StateEnum.IDLE.name());
                boolean updateActivationState = droneManager.updateActivationStatus(true, drone.getId());

                publishMessage(insertStatus, updateActivationState, atomicReference, drone);
            });

        }
    }

    private void lockActivateNewlyAddedDroneProcess(Property property) {
        try{
            propertyManager.updateProperty("false", property.getId());

        }catch(MusalaLogisticsException musalaLogisticsException){
            throw new MusalaLogisticsException(musalaLogisticsException.getMessage(), musalaLogisticsException.getCause());
        }
    }

    private boolean droneActivationProcessingEnabled() {
        Optional<Property> property;

        try{
            property = propertyManager.getPropertyByKey(AppConstants.DRONE_NEW_ACTIVATION_PROCESS_ENABLED);

        }catch (MusalaLogisticsException musalaLogisticsException){
            throw new MusalaLogisticsException(musalaLogisticsException.getMessage(), musalaLogisticsException.getCause());
        }

        property.orElseThrow();
        this.property = property.get();

        return !property.get().getState().equals("false");
    }
    
    private void publishMessage(boolean insertStatus, boolean updateActivationState, AtomicReference<String> atomicReference, Drone drone){
        if(insertStatus && updateActivationState){
            atomicReference.set(String.format(" Drone with id %d and serial number %s successfully activated", drone.getId(), drone.getSerial()));
            LOG.info(atomicReference.get());
        }
    }
    
    @PreDestroy
    public void destroy(){
        LOG.info(" This job instance is being destroyed ");
    }

}
