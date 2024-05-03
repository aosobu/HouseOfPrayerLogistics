package com.spiritcoder.musalalogistics.commons.cache;

import org.springframework.cache.Cache;

import java.util.Collection;
import java.util.Optional;

public interface CacheManager {

    boolean ping();

    <T> boolean insert(String key, T object, String cacheName);

    Optional<Object> get(String key, String cacheName);

    Cache getCache(String name);

    Collection<String> getCacheNames();

    boolean evictIfPresent(String key, String cacheName);
}
