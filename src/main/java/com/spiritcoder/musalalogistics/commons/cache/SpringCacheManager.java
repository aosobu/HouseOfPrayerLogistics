package com.spiritcoder.musalalogistics.commons.cache;

import com.spiritcoder.musalalogistics.commons.exception.MusalaLogisticsException;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.Cache;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Objects;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class SpringCacheManager implements CacheManager {

    private final org.springframework.cache.CacheManager cacheManager;

    @Override
    public boolean ping() {
        return false;
    }

    @Override
    public <T> boolean insert(String key, T object, String cacheName) {
        try{
            if(isCacheExist(cacheName)) {
                Objects.requireNonNull(cacheManager.getCache(cacheName)).put(key, object);
            }
        }catch(MusalaLogisticsException musalaLogisticsException){
            return false;
        }
        return true;
    }

    private boolean isCacheExist(String cacheName) {
        return !Objects.isNull(cacheManager.getCache(cacheName));
    }

    @Override
    public Optional<Object> get(String key, String cacheName) {
        try{
            if(isCacheExist(cacheName)) {
                return Optional.ofNullable(Objects.requireNonNull(cacheManager.getCache(cacheName)).get(key));
            }
        }catch(MusalaLogisticsException musalaLogisticsException){
            return Optional.empty();
        }
        return Optional.empty();
    }

    public Cache getCache(String name) {
        return cacheManager.getCache(name);
    }

    public Collection<String> getCacheNames() {
        return cacheManager.getCacheNames();
    }

    @Override
    public boolean evictIfPresent(String key, String cacheName) {
        return Objects.requireNonNull(cacheManager.getCache(cacheName)).evictIfPresent(key);
    }
}
