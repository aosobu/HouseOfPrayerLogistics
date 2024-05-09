package com.spiritcoder.musalalogistics.workers.scheduler;

import com.spiritcoder.musalalogistics.workers.model.QuartzTimerInfo;
import lombok.RequiredArgsConstructor;

import java.util.Objects;

@RequiredArgsConstructor
public class QuartzScheduler {

    private final QuartzSchedulerService scheduler;

    public void scheduleJob(final Class jobClass, QuartzTimerInfo info) {
        info = getTimerInfoDetails(info, jobClass.getName());
        scheduler.schedule(jobClass, info);
    }

    private QuartzTimerInfo getTimerInfoDetails(QuartzTimerInfo info, String className) {
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
        //TODO:: get cron expression from cache before reverting to database
        return null;
    }
}
