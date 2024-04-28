package com.spiritcoder.musalalogistics.job.jobimpl;


import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;


@Component
public class ActivateNewlyAddedDronesJob implements Job {

    private static final Logger LOG = LoggerFactory.getLogger(ActivateNewlyAddedDronesJob.class);

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        // retrieve all newly registered drones that not yet activated
        // insert into
        LOG.info(" we are making progress bit by bit =====> ");
    }

}
