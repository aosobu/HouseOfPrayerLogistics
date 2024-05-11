package com.spiritcoder.musalalogistics.commons.cache;

import com.spiritcoder.musalalogistics.commons.util.NetworkUtil;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RKeys;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class RedisCacheManager implements CacheManager{

    @Value("${server.host.ip}")
    private final String ip = "127.0.0.1";

    private final Integer port = 6379;

    private final RedissonClient redissonClient;

    @Override
    public boolean ping() {
        return NetworkUtil.ping(ip, port);
    }

    public <T> boolean insert(String key, T object, String cacheName) {
        try{
            RMap<String, Object> map = redissonClient.getMap(cacheName);
            map.put(key, object);
        }catch (Exception ex){
            return false;
        }
        return true;
    }

    public Optional<Object> get(String key, String cacheName) {
        try{
            RMap<String, Object> map = redissonClient.getMap(cacheName);
            return Optional.of(map.get(key));
        }catch (Exception ex){
            return Optional.empty();
        }
    }

    @Override
    public Collection<String> getCacheNames() {
        return List.of();
    }

    @Override
    public RKeys getCacheKeys() {
        return redissonClient.getKeys();
    }

    public boolean isExists(String name) {
        try{
            redissonClient.getMap(name).isExists();
            return true;
        }catch(Exception exception){
            return false;
        }
    }

    @Override
    public boolean isCacheAvailable(String cacheName) {
        return ping() || isExists(cacheName);
    }

    public boolean evictIfPresent(String key, String cacheName) {
        return false;
    }

    @Override
    public boolean evictIfPresent(Object object, String cacheName) {
        redissonClient.getMap(cacheName).remove(object);
        return false;
    }
}
