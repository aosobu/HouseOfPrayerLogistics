package com.spiritcoder.musalalogistics.workers;

import com.spiritcoder.musalalogistics.commons.config.AppConstants;
import com.spiritcoder.musalalogistics.commons.exception.MusalaLogisticsException;
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

        }catch (MusalaLogisticsException musalaLogisticsException){

            String message = String.format("%s was not scheduled with %s", jobClass.getSimpleName(), schedulerType);
            LOG.error(message);
            throw new MusalaLogisticsException(musalaLogisticsException.getMessage(), musalaLogisticsException.getCause());
        }

        String message = String.format("%s was scheduled with %s", jobClass.getSimpleName(), schedulerType);
        LOG.info(message);
        schedulerType = SchedulerTypeEnum.QUARTZ.toString(); // reset to default
    }
}
