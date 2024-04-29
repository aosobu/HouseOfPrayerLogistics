package com.spiritcoder.musalalogistics.job.batchjobs;

import com.spiritcoder.musalalogistics.commons.config.AppConstants;
import com.spiritcoder.musalalogistics.commons.entity.Property;
import com.spiritcoder.musalalogistics.commons.exception.MusalaLogisticsException;
import com.spiritcoder.musalalogistics.commons.repository.PropertyManager;
import com.spiritcoder.musalalogistics.drone.entity.DroneStateSnapshot;
import com.spiritcoder.musalalogistics.drone.enums.StateEnum;
import com.spiritcoder.musalalogistics.drone.repository.DroneManager;
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

@Component
@RequiredArgsConstructor
public class DroneLazyLoaderJob implements Job {

    private static final Logger LOG = LoggerFactory.getLogger(DroneLazyLoaderJob.class);

    private final DroneManager droneManager;

    private final PropertyManager propertyManager;

    private Property property;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        try{

            if(!isDroneLoadingProcessingEnabled()) return; //prevents race condition

            lockDroneLoadingProcess(property);

            Optional<List<DroneStateSnapshot>> loadableDrones = droneManager.findAllLoadableDrones();

            loadableDrones.orElseThrow();

            loadDrone(loadableDrones.get());



        }catch(MusalaLogisticsException musalaLogisticsException){

            throw new MusalaLogisticsException(musalaLogisticsException.getMessage(), musalaLogisticsException.getCause());

        }finally{


        }
    }

    @Transactional
    private void loadDrone(List<DroneStateSnapshot> loadableDrones) {

        loadableDrones.forEach(drone -> {
            boolean updateDroneState = droneManager.updateDroneStateSnapshot(StateEnum.LOADING.name(), drone.getId());

            //get medications for loading


        });
    }

    private void lockDroneLoadingProcess(Property property) {
        try{
            propertyManager.updateProperty("false", property.getId());

        }catch(MusalaLogisticsException musalaLogisticsException){
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

        }catch (MusalaLogisticsException musalaLogisticsException){
            throw new MusalaLogisticsException(musalaLogisticsException.getMessage(), musalaLogisticsException.getCause());
        }

        property.orElseThrow();
        this.property = property.get();

        return !property.get().getState().equals("false");
    }
}
