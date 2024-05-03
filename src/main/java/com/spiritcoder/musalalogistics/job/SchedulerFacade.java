package com.spiritcoder.musalalogistics.job;

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
        String SCHEDULE_SUFFIX = "JobSchedule";

        try{
            if (schedulerTypeEnum != null) {
                schedulerType = "";
                schedulerType = schedulerTypeEnum.toString();
            }
            schedulerType = schedulerType.toLowerCase().concat(SCHEDULE_SUFFIX);
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
