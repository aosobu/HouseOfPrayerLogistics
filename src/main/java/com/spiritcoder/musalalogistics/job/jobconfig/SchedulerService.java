package com.spiritcoder.musalalogistics.job.jobconfig;

import com.spiritcoder.musalalogistics.job.HelloWorldJob;
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
public class SchedulerService {

    private static final Logger LOG = LoggerFactory.getLogger(HelloWorldJob.class);

    private final Scheduler scheduler;

    public  void schedule(final Class jobClass, final TimerInfo info) {
        final JobDetail jobDetail = TimerUtils.buildJobDetail(jobClass, info);
        final Trigger trigger = TimerUtils.buildTrigger(jobClass, info);

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
        }catch (SchedulerException schedulerException){
            LOG.error(schedulerException.getMessage());
        }
    }

    @PreDestroy
    public void preDestroy(){
        try{
            scheduler.shutdown();
        }catch (SchedulerException schedulerException){
            LOG.error(schedulerException.getMessage());
        }
    }

}
