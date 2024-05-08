package com.spiritcoder.musalalogistics.workers;

import com.spiritcoder.musalalogistics.workers.model.QuartzTimerInfo;
import com.spiritcoder.musalalogistics.workers.scheduler.QuartzScheduler;
import com.spiritcoder.musalalogistics.workers.scheduler.QuartzSchedulerService;
import lombok.RequiredArgsConstructor;
import org.quartz.Scheduler;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class QuartzJobScheduler implements JobScheduler {

    private final Scheduler scheduler;

    @Override
    public void scheduleJob(Class jobClass) {
        QuartzSchedulerService quartzSchedulerService = new QuartzSchedulerService(scheduler);
        QuartzScheduler quartzScheduler = new QuartzScheduler(quartzSchedulerService);
        QuartzTimerInfo info = new QuartzTimerInfo();
        quartzScheduler.scheduleJob(jobClass, info);
    }
}
