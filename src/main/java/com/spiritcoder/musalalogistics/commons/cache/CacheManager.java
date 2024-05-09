package com.spiritcoder.musalalogistics.commons.cache;


import java.util.Collection;
import java.util.Optional;

public interface CacheManager {

    boolean ping();

    <T> boolean insert(String key, T object, String cacheName);

    Optional<Object> get(String key, String cacheName);

    Collection<String> getCacheNames();

    <T> T getCacheKeys();

    boolean evictIfPresent(String key, String cacheName);

    <T> boolean evictIfPresent(T object, String cacheName);

    boolean isExists(String name);
}
