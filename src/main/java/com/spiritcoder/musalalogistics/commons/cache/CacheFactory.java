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

    @Autowired
    public CacheFactory(org.springframework.cache.CacheManager cacheManager, RedissonClient redissonClient) {
        this.cacheManager = cacheManager;
        this.redissonClient = redissonClient;
    }

    public CacheManager getCacheManager(CacheTypeEnum cacheTypeEnum) {
        if(cacheTypeEnum.equals(CacheTypeEnum.SPRING_CACHE)){
            return getDefaultCacheManager();
        }else if(cacheTypeEnum.equals(CacheTypeEnum.REDIS)){
            return new RedisCacheManager(redissonClient);
        }
        return new SpringCacheManager(cacheManager);
    }

    private CacheManager getDefaultCacheManager() {
        return new SpringCacheManager(cacheManager);
    }
}
