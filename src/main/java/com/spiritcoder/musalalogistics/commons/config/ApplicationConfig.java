package com.spiritcoder.musalalogistics.commons.config;

import com.spiritcoder.musalalogistics.commons.entity.Property;
import com.spiritcoder.musalalogistics.commons.repository.PropertyManager;
import com.spiritcoder.musalalogistics.identity.users.repository.UserRepository;
import com.spiritcoder.musalalogistics.workers.JobScheduler;
import com.spiritcoder.musalalogistics.workers.SchedulerFacade;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.CacheManager;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.*;

@Configuration
@RequiredArgsConstructor
public class ApplicationConfig {

    private final SchedulerFacade schedulerFacade;

    private final PropertyManager propertyManager;

    private final UserRepository userRepository;

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

    @Bean
    public UserDetailsService userDetailsService() {
        return username -> userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @PostConstruct
    public void initScheduler(){
        //schedulerFacade.scheduleJob(DroneLazyLoaderJob.class, null);
    }

    private String[] getCacheList(){
        List<Property> propertyList;
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
