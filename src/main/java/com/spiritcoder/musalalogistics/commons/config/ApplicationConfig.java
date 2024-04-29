package com.spiritcoder.musalalogistics.commons.config;

import com.spiritcoder.musalalogistics.job.JobSchedule;
import com.spiritcoder.musalalogistics.job.SchedulerFacade;
import com.spiritcoder.musalalogistics.job.batchjobs.ActivateNewlyAddedDronesJob;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
public class ApplicationConfig {

    @Autowired
    private SchedulerFacade schedulerFacade;

    @Bean("jobScheduleMap")
    public Map<String, JobSchedule> jobScheduleMap(List<JobSchedule> jobScheduleList){
        Map<String, JobSchedule> jobScheduleMap = new HashMap<>();
        jobScheduleList.forEach(jobSchedule -> jobScheduleMap.put(jobSchedule.getClass().getSimpleName().toLowerCase(), jobSchedule));
        return jobScheduleMap;
    }

    @PostConstruct
    public void initScheduler(){
        schedulerFacade.scheduleJob(ActivateNewlyAddedDronesJob.class, null);
    }

}
