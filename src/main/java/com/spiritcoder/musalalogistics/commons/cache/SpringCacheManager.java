package com.spiritcoder.musalalogistics.commons.cache;

import com.spiritcoder.musalalogistics.commons.util.NetworkUtil;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RKeys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.Cache;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Objects;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class SpringCacheManager implements CacheManager {

    private final org.springframework.cache.CacheManager cacheManager;

    @Value("${server.host.ip}")
    private String ip = "127.0.0.1";

    @Value("${spring.server.port}")
    private Integer port;

    @Override
    public boolean ping() {
         return NetworkUtil.ping(ip, port);
    }

    @Override
    public <T> boolean insert(String key, T object, String cacheName) {
        try{
            if(isCacheExist(cacheName)) {
                Objects.requireNonNull(cacheManager.getCache(cacheName)).put(key, object);
            }
        }catch(Exception Exception){
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
        }catch(Exception Exception){
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
    public RKeys getCacheKeys() {
        return null;
    }

    @Override
    public boolean evictIfPresent(String key, String cacheName) {
        return Objects.requireNonNull(cacheManager.getCache(cacheName)).evictIfPresent(key);
    }

    @Override
    public <T> boolean evictIfPresent(T object, String cacheName) {
        return false;
    }

    @Override
    public boolean isExists(String name) {
        return false;
    }

    @Override
    public boolean isCacheAvailable(String cacheName) {
        return ping() || isCacheExist(cacheName);
    }
}
