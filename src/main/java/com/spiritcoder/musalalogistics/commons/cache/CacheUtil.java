package com.spiritcoder.musalalogistics.commons.cache;

import org.springframework.stereotype.Component;

@Component
public class CacheUtil {

    public static String generateKey(String key){
        return "#".concat(key);
    }
}
