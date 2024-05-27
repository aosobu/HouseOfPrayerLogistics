package com.spiritcoder.musalalogistics.workers;

import com.spiritcoder.musalalogistics.commons.config.AppConstants;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class SchedulerFacade {

    private static final Logger LOG = LoggerFactory.getLogger(SchedulerFacade.class);

    private final Map< String , JobScheduler> jobScheduleMap;

    private String schedulerType = SchedulerTypeEnum.QUARTZ.toString(); //default scheduler

    public <T> void scheduleJob(final Class<T> jobClass, SchedulerTypeEnum schedulerTypeEnum){

        try{
            if (schedulerTypeEnum != null) {
                schedulerType = "";
                schedulerType = schedulerTypeEnum.toString();
            }
            schedulerType = schedulerType.toLowerCase().concat(AppConstants.SCHEDULE_SUFFIX);
            jobScheduleMap.get(schedulerType).scheduleJob(jobClass);

        }catch (Exception Exception){

            String message = String.format("%s was not scheduled with %s", jobClass.getSimpleName(), schedulerType);
            publishMessage(message);
            throw new RuntimeException(Exception.getMessage(), Exception.getCause());
        }

        String message = String.format("%s was scheduled with %s", jobClass.getSimpleName(), schedulerType);
        publishMessage(message);
        schedulerType = SchedulerTypeEnum.QUARTZ.toString(); // reset to default
    }

    private <T> void publishMessage(String message){
        LOG.info(message);
    }
}
