package com.spiritcoder.musalalogistics.commons.cache;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CacheFactory {

    private org.springframework.cache.CacheManager cacheManager;

    @Autowired
    public CacheFactory(org.springframework.cache.CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    public CacheManager getCacheManager(CacheTypeEnum cacheTypeEnum) {
        if(cacheTypeEnum.equals(CacheTypeEnum.SPRING_CACHE)){
            return getDefaultCacheManager();
        }
        return new SpringCacheManager(cacheManager);
    }

    private CacheManager getDefaultCacheManager() {
        return new SpringCacheManager(cacheManager);
    }
}
