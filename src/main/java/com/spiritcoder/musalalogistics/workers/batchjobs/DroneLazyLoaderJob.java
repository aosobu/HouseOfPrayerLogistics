package com.spiritcoder.musalalogistics.workers.batchjobs;

import com.spiritcoder.musalalogistics.commons.config.AppConstants;
import com.spiritcoder.musalalogistics.commons.entity.Property;
import com.spiritcoder.musalalogistics.commons.repository.PropertyManager;
import com.spiritcoder.musalalogistics.droneservice.enums.LoadTypeEnum;
import com.spiritcoder.musalalogistics.droneservice.service.component.DroneLoaderComponent;
import lombok.RequiredArgsConstructor;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class DroneLazyLoaderJob implements Job {

    private static final Logger LOG = LoggerFactory.getLogger(DroneLazyLoaderJob.class);

    private final PropertyManager propertyManager;

    private final DroneLoaderComponent droneLoader;

    private Property property;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {

        boolean processLock = false;

        try{

            if(!isDroneLoadingProcessingEnabled()){return;} //prevents race condition

            processLock = lockDroneLoadingProcess(property);

            droneLoader.loadDrone(null, LoadTypeEnum.LAZY);

            unlockDroneOnboardingProcess(property);

        }catch(Exception Exception){
            throw new RuntimeException(Exception.getMessage());

        }finally{
            if(processLock){
                unlockDroneOnboardingProcess(property);
            }
        }
    }



    private void unlockDroneOnboardingProcess(Property property) {
        try{
            propertyManager.updateProperty("true", property.getId());

        }catch(Exception Exception){

            LOG.error(Exception.getMessage(), Exception.getCause());
            throw new RuntimeException(Exception.getMessage());
        }
    }

    private boolean lockDroneLoadingProcess(Property property) {
        try{
            propertyManager.updateProperty("false", property.getId());
            return true;

        }catch(Exception Exception){

            LOG.error(Exception.getMessage(), Exception.getCause());
            throw new RuntimeException(Exception.getMessage());
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

        }catch (Exception Exception){

            LOG.error(Exception.getMessage(), Exception.getCause());
            throw new RuntimeException(Exception.getMessage());
        }

        return !property.get().getState().equals("false");
    }
}
