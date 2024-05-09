package com.spiritcoder.musalalogistics.workers.batchjobs;

import lombok.RequiredArgsConstructor;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BatteryAuditJob implements Job {

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        //TODO:: job to perform battery audit - which typically involves
        // TODO:: 1. registering an audit event (BATTERY_AUDIT) and migration of battery data to audit tables
    }
}
