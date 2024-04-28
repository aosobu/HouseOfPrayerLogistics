package com.spiritcoder.musalalogistics.job.jobconfig;

import lombok.RequiredArgsConstructor;

import java.util.Objects;

@RequiredArgsConstructor
public class JobScheduler {

    private final SchedulerService scheduler;

    public void scheduleJob(final Class jobClass, TimerInfo info) {
        info = getTimerInfoDetails(info, jobClass.getName());
        scheduler.schedule(jobClass, info);
    }

    private TimerInfo getTimerInfoDetails(TimerInfo info, String className) {
        info.setCronExpression(getCronExpression(className));
        return info;
    }

    private String getCronExpression(String className){
        String cronExpression = getReloadableProperty(className);
        String DEFAULT_TIMER = "0 0/1 * 1/1 * ? *";
        if(Objects.isNull(cronExpression))
            return DEFAULT_TIMER;
        return cronExpression;
    }

    private String getReloadableProperty(String propertyKey) {
        // try to get cron expression from cache before reverting to database
        return null;
    }
}
