package com.spiritcoder.musalalogistics.job;

import com.spiritcoder.musalalogistics.job.model.QuartzTimerInfo;
import com.spiritcoder.musalalogistics.job.scheduler.QuartzScheduler;
import com.spiritcoder.musalalogistics.job.scheduler.QuartzSchedulerService;
import org.quartz.Scheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class QuartzJobSchedule implements JobSchedule {

    @Autowired
    private Scheduler scheduler;

    @Override
    public void scheduleJob(Class jobClass) {
        QuartzSchedulerService quartzSchedulerService = new QuartzSchedulerService(scheduler);
        QuartzScheduler quartzScheduler = new QuartzScheduler(quartzSchedulerService);
        QuartzTimerInfo info = new QuartzTimerInfo();
        quartzScheduler.scheduleJob(jobClass, info);
    }
}
