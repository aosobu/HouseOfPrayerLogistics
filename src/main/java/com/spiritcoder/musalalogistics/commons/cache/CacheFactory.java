package com.spiritcoder.musalalogistics.commons.cache;

import lombok.RequiredArgsConstructor;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CacheFactory {

    private org.springframework.cache.CacheManager cacheManager;

    private RedissonClient redissonClient;

    private CacheManager SPRING_CACHE_MANAGER_INSTANCE;

    private CacheManager REDIS_CACHE_MANAGER_INSTANCE;

    @Autowired
    private CacheFactory(org.springframework.cache.CacheManager cacheManager, RedissonClient redissonClient) {
        this.cacheManager = cacheManager;
        this.redissonClient = redissonClient;
    }

    public synchronized CacheManager getCacheManager(CacheTypeEnum cacheTypeEnum) {

        if(SPRING_CACHE_MANAGER_INSTANCE != null && isSpringCacheType(cacheTypeEnum)){
            return SPRING_CACHE_MANAGER_INSTANCE;
        }

        if(REDIS_CACHE_MANAGER_INSTANCE != null && isRedisCacheType(cacheTypeEnum)){
            return REDIS_CACHE_MANAGER_INSTANCE;
        }

        if(isSpringCacheType(CacheTypeEnum.SPRING_CACHE)){

            SPRING_CACHE_MANAGER_INSTANCE =  getDefaultCacheManager();
            return SPRING_CACHE_MANAGER_INSTANCE;

        }else if(isRedisCacheType(CacheTypeEnum.REDIS)){

            REDIS_CACHE_MANAGER_INSTANCE =  new RedisCacheManager(redissonClient);
            return REDIS_CACHE_MANAGER_INSTANCE;

        }

        return new SpringCacheManager(cacheManager);
    }

    private boolean isRedisCacheType(CacheTypeEnum cacheTypeEnum) {
        return cacheTypeEnum.equals(CacheTypeEnum.REDIS);
    }

    private boolean isSpringCacheType(CacheTypeEnum cacheTypeEnum) {
        return cacheTypeEnum.equals(CacheTypeEnum.SPRING_CACHE);
    }

    private CacheManager getDefaultCacheManager() {
        return new SpringCacheManager(cacheManager);
    }
}
