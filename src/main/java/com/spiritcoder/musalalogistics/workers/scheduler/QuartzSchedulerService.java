package com.spiritcoder.musalalogistics.workers.scheduler;

import com.spiritcoder.musalalogistics.workers.model.QuartzTimerInfo;
import com.spiritcoder.musalalogistics.workers.utils.QuartzTimerUtils;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RequiredArgsConstructor
public class QuartzSchedulerService {

    private static final Logger LOG = LoggerFactory.getLogger(QuartzSchedulerService.class);

    private final Scheduler scheduler;

    public  void schedule(final Class jobClass, final QuartzTimerInfo info) {
        final JobDetail jobDetail = QuartzTimerUtils.buildJobDetail(jobClass, info);
        final Trigger trigger = QuartzTimerUtils.buildTrigger(jobClass, info);

        try {
            scheduler.scheduleJob(jobDetail, trigger);
        } catch (SchedulerException e) {
            LOG.error(e.getMessage(), e);
        }
    }

    @PostConstruct
    public void init(){
        try{
            scheduler.start();
            LOG.info("scheduler staring up .....");
        }catch (SchedulerException schedulerException){
            LOG.error(schedulerException.getMessage());
        }
    }

    @PreDestroy
    public void preDestroy(){
        try{
            scheduler.shutdown();
            LOG.info("schdeuler shutting down ......");
        }catch (SchedulerException schedulerException){
            LOG.error(schedulerException.getMessage());
        }
    }

}
