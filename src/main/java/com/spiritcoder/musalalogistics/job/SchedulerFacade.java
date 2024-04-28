package com.spiritcoder.musalalogistics.job;

import com.spiritcoder.musalalogistics.commons.exception.MusalaLogisticsException;
import com.spiritcoder.musalalogistics.job.jobimpl.ActivateNewlyAddedDronesJob;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class SchedulerFacade {

    private static final Logger LOG = LoggerFactory.getLogger(SchedulerFacade.class);
    private final Map< String , JobSchedule> jobScheduleMap;

    private String scheduleType = ScheduleTypeEnum.QUARTZ.toString();

    @Autowired
    public SchedulerFacade(Map<String, JobSchedule> jobScheduleMap) {
        this.jobScheduleMap = jobScheduleMap;
    }

    public void scheduleJob(final Class jobClass, ScheduleTypeEnum scheduleTypeEnum){
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
