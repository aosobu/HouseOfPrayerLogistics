package com.spiritcoder.musalalogistics;

import com.spiritcoder.musalalogistics.job.HelloWorldJob;
import com.spiritcoder.musalalogistics.job.jobconfig.JobScheduler;
import com.spiritcoder.musalalogistics.job.jobconfig.SchedulerService;
import com.spiritcoder.musalalogistics.job.jobconfig.TimerInfo;
import org.quartz.Scheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MusalaLogisticsApplication implements CommandLineRunner {

	@Autowired
	private Scheduler scheduler;

	public static void main(String[] args) {
		SpringApplication.run(MusalaLogisticsApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		SchedulerService schedulerService = new SchedulerService(scheduler);

		JobScheduler jobScheduler = new JobScheduler(schedulerService);
		TimerInfo info = new TimerInfo();

		jobScheduler.scheduleJob(HelloWorldJob.class, info);
	}
}
