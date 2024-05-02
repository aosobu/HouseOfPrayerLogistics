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

    private final Map< String , JobSchedule> jobScheduleMap;

    private String scheduleType = ScheduleTypeEnum.QUARTZ.toString(); //defaults to QUARTZ

    public <T> void scheduleJob(final Class<T> jobClass, ScheduleTypeEnum scheduleTypeEnum){
        String SCHEDULE_SUFFIX = "JobSchedule";

        try{
            if (scheduleTypeEnum != null) {
                scheduleType = scheduleTypeEnum.toString();
            }
            scheduleType = scheduleType.toLowerCase().concat(SCHEDULE_SUFFIX);
            jobScheduleMap.get(scheduleType).scheduleJob(jobClass);

        }catch (MusalaLogisticsException musalaLogisticsException){
            throw new MusalaLogisticsException(musalaLogisticsException.getMessage(), musalaLogisticsException.getCause());
        }

        String message = String.format("%s was scheduled with %s", jobClass.getSimpleName(), scheduleType);
        LOG.info(message);
    }
}
