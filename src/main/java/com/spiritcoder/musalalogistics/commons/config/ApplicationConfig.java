package com.spiritcoder.musalalogistics.commons.config;

import com.spiritcoder.musalalogistics.job.JobSchedule;
import com.spiritcoder.musalalogistics.job.SchedulerFacade;
import com.spiritcoder.musalalogistics.job.batchjobs.DroneLazyLoaderJob;
import com.spiritcoder.musalalogistics.job.batchjobs.DroneOnboardingJob;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class ApplicationConfig {

    private final SchedulerFacade schedulerFacade;

    @Bean("jobScheduleMap")
    public Map<String, JobSchedule> jobScheduleMap(List<JobSchedule> jobScheduleList){
        Map<String, JobSchedule> jobScheduleMap = new HashMap<>();
        jobScheduleList.forEach(jobSchedule -> jobScheduleMap.put(jobSchedule.getClass().getSimpleName().toLowerCase(), jobSchedule));
        return jobScheduleMap;
    }

    @PostConstruct
    public void initScheduler(){
        schedulerFacade.scheduleJob(DroneOnboardingJob.class, null);
        schedulerFacade.scheduleJob(DroneLazyLoaderJob.class, null);
    }

}
