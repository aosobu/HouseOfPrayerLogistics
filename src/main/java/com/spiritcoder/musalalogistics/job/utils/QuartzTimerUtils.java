package com.spiritcoder.musalalogistics.job.utils;

import com.spiritcoder.musalalogistics.job.model.QuartzTimerInfo;
import org.quartz.*;

public final class QuartzTimerUtils {

        private QuartzTimerUtils() {}

        public static JobDetail buildJobDetail(final Class jobClass, final QuartzTimerInfo info) {
            final JobDataMap jobDataMap = new JobDataMap();
            jobDataMap.put(jobClass.getSimpleName(), info);

            return JobBuilder
                    .newJob(jobClass)
                    .withIdentity(jobClass.getSimpleName())
                    .setJobData(jobDataMap)
                    .build();
        }

        public static Trigger buildTrigger(final Class jobClass, final QuartzTimerInfo info) {
            CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(info.getCronExpression());

            return TriggerBuilder
                    .newTrigger()
                    .withIdentity(jobClass.getSimpleName())
                    .withSchedule(cronScheduleBuilder)
                    .build();
        }
}
