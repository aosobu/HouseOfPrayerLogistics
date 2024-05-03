package com.spiritcoder.musalalogistics.commons.repository;

import com.spiritcoder.musalalogistics.commons.entity.Property;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface PropertyRepository extends JpaRepository<Property, Integer> {

    @Query(value = "select * from property where property = ?1", nativeQuery = true)
    Optional<Property> getPropertyByKey(String key);

    @Modifying
    @Transactional
    @Query(value = "update property set state = ?1 where id = ?2", nativeQuery = true)
    void updateProperty(String value, Integer id);

    @Query(value = "select * from property where property = ?1", nativeQuery = true)
    Optional<List<Property>> getPropertyListByKey(String key);
}
