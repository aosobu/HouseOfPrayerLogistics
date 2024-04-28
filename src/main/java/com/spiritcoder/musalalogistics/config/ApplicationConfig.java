package com.spiritcoder.musalalogistics.config;

import com.spiritcoder.musalalogistics.job.JobSchedule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
public class ApplicationConfig {

    @Bean("jobScheduleMap")
    public Map<String, JobSchedule> jobScheduleMap(List<JobSchedule> jobScheduleList){
        Map<String, JobSchedule> jobScheduleMap = new HashMap<>();
        jobScheduleList.forEach(jobSchedule -> jobScheduleMap.put(jobSchedule.getClass().getSimpleName().toLowerCase(), jobSchedule));
        return jobScheduleMap;
    }

}
