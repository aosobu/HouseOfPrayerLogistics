package com.spiritcoder.musalalogistics.commons.repository;

import com.spiritcoder.musalalogistics.commons.entity.Property;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PropertyManagerImpl implements PropertyManager{

    private final PropertyRepository propertyRepository;

    @Override
    public Optional<Property> getPropertyByKey(String key) {
        return propertyRepository.getPropertyByKey(key);
    }

    @Override
    public Optional<List<Property>> getPropertyListByKey(String key) {
        return propertyRepository.getPropertyListByKey(key);
    }

    @Override
    public void  updateProperty(String value, Integer id) {
        try{
            propertyRepository.updateProperty(value, id);

        }catch(Exception Exception){
            throw new RuntimeException(Exception.getMessage(), Exception.getCause());
        }
    }
}
