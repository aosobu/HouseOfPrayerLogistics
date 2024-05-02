package com.spiritcoder.musalalogistics.job.batchjobs;


import com.spiritcoder.musalalogistics.commons.config.AppConstants;
import com.spiritcoder.musalalogistics.commons.entity.Property;
import com.spiritcoder.musalalogistics.commons.exception.MusalaLogisticsException;
import com.spiritcoder.musalalogistics.commons.repository.PropertyManager;
import lombok.RequiredArgsConstructor;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * This class simulates the addition of battery data to the database.
 * In a real world application, an endpoint would be available for the
 * device to send its monitored data
 */
@Component
@RequiredArgsConstructor
public class BatteryLevelSnapshotJob implements Job {

    private final PropertyManager propertyManager;

    private Property property;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {

        try{

            if(!isDroneBatteryProcessingEnabled()) return; //prevents race condition

            lockDroneBatteryProcess(property);

            addDroneBatteryDataToDB();

            unlockDroneBatteryProcess(property);

        }catch(MusalaLogisticsException musalaLogisticsException){

            throw new MusalaLogisticsException(musalaLogisticsException.getMessage(), musalaLogisticsException.getCause());
        }finally {

            unlockDroneBatteryProcess(property);
        }

    }

    /**
     * This method assumes that battery data is received at once from all devices.
     * However, the generic predicate of the algorithm suffices
     * Battery data is archived in the
     */
    private void addDroneBatteryDataToDB() {
        //get drones from dronebatterysnapshot table


    }

    private void unlockDroneBatteryProcess(Property property) {
        try{
            propertyManager.updateProperty("true", property.getId());

        }catch(MusalaLogisticsException musalaLogisticsException){
            throw new MusalaLogisticsException(musalaLogisticsException.getMessage(), musalaLogisticsException.getCause());
        }
    }

    private void lockDroneBatteryProcess(Property property) {
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
    private boolean isDroneBatteryProcessingEnabled() {
        Optional<Property> property;

        try{
            property = propertyManager.getPropertyByKey(AppConstants.DRONE_BATTERY_PROCESS_ENABLED);

        }catch (MusalaLogisticsException musalaLogisticsException){
            throw new MusalaLogisticsException(musalaLogisticsException.getMessage(), musalaLogisticsException.getCause());
        }

        property.orElseThrow();
        this.property = property.get();

        return !property.get().getState().equals("false");
    }

}
