package com.spiritcoder.musalalogistics.job;


import com.spiritcoder.musalalogistics.job.jobconfig.TimerInfo;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;


@Component
public class HelloWorldJob implements Job {

    private static final Logger LOG = LoggerFactory.getLogger(HelloWorldJob.class);

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {

        JobDataMap jobDataMap = jobExecutionContext.getJobDetail().getJobDataMap();
        TimerInfo timerInfo = (TimerInfo) jobDataMap.get(HelloWorldJob.class.getSimpleName());

        LOG.info("Hello World Job with call back data as  : " + timerInfo.getCallbackData() );
    }

}
