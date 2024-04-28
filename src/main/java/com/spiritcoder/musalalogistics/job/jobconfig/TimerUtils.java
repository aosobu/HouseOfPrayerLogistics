package com.spiritcoder.musalalogistics.job.jobconfig;

import org.quartz.*;

public final class TimerUtils {

        private TimerUtils() {}

        public static JobDetail buildJobDetail(final Class jobClass, final TimerInfo info) {
            final JobDataMap jobDataMap = new JobDataMap();
            jobDataMap.put(jobClass.getSimpleName(), info);

            return JobBuilder
                    .newJob(jobClass)
                    .withIdentity(jobClass.getSimpleName())
                    .setJobData(jobDataMap)
                    .build();
        }

        public static Trigger buildTrigger(final Class jobClass, final TimerInfo info) {
            CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(info.getCronExpression());

            return TriggerBuilder
                    .newTrigger()
                    .withIdentity(jobClass.getSimpleName())
                    .withSchedule(cronScheduleBuilder)
                    .build();
        }
}
