package com.spiritcoder.musalalogistics.commons.config;

import com.spiritcoder.musalalogistics.commons.entity.Property;
import com.spiritcoder.musalalogistics.commons.repository.PropertyManager;
import com.spiritcoder.musalalogistics.job.JobScheduler;
import com.spiritcoder.musalalogistics.job.SchedulerFacade;
import com.spiritcoder.musalalogistics.job.batchjobs.DroneLazyLoaderJob;
import com.spiritcoder.musalalogistics.job.batchjobs.DroneOnboardingJob;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.CacheManager;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.*;

@Configuration
@RequiredArgsConstructor
public class ApplicationConfig {

    private final SchedulerFacade schedulerFacade;

    private final PropertyManager propertyManager;

    @Bean("jobScheduleMap")
    public Map<String, JobScheduler> jobScheduleMap(List<JobScheduler> jobSchedulerList){
        Map<String, JobScheduler> jobScheduleMap = new HashMap<>();
        jobSchedulerList.forEach(jobScheduler -> jobScheduleMap.put(jobScheduler.getClass().getSimpleName().toLowerCase(), jobScheduler));
        return jobScheduleMap;
    }

    @Bean()
    public CacheManager cacheManager() {
        if(Objects.isNull(getCacheList())) {
            return new ConcurrentMapCacheManager();
        }
        return new ConcurrentMapCacheManager(getCacheList());
    }

    @PostConstruct
    public void initScheduler(){
//        schedulerFacade.scheduleJob(DroneOnboardingJob.class, null);
//        schedulerFacade.scheduleJob(DroneLazyLoaderJob.class, null);
    }

    private String[] getCacheList(){
        List<Property> propertyList = new ArrayList<>();
        String [] cacheArray = null;

        Optional<List<Property>> caches = propertyManager.getPropertyListByKey(AppConstants.CACHES);

        if(caches.isPresent()){
            propertyList = caches.get();
            cacheArray = new String[propertyList.size()];

            for (int i = 0; i < propertyList.size(); i++) {
                cacheArray[i] = propertyList.get(i).getState();
            }
        }

        return cacheArray;
    }

}
