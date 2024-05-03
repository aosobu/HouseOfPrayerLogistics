package com.spiritcoder.musalalogistics.commons.repository;

import com.spiritcoder.musalalogistics.commons.entity.Property;

import java.util.List;
import java.util.Optional;

public interface PropertyManager {

    Optional<Property> getPropertyByKey(String key);

    Optional<List<Property>> getPropertyListByKey(String key);

    void updateProperty(String key, Integer id);
}
