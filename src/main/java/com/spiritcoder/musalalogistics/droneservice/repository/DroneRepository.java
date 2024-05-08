package com.spiritcoder.musalalogistics.droneservice.repository;

import com.spiritcoder.musalalogistics.droneservice.entity.Drone;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DroneRepository extends JpaRepository<Drone, Integer> {

    @Query(value = "select * from drone where activated = 'false'", nativeQuery = true)
    Optional<List<Drone>> findAllInactiveDrones();

    @Query(value = "select * from drone where activated = 'true'", nativeQuery = true)
    Optional<List<Drone>> findAllActiveDrones();

    @Modifying
    @Transactional
    @Query(value = "update drone set activated = ?1 where id = ?2 ", nativeQuery = true)
    void updateActivationStatus(boolean state, Integer droneId);

    @Modifying
    @Transactional
    @Query(value = "insert into drone(serial, model, weight, creator, updater, created) values (?1, ?2, ?3, ?4, ?5, getDate())", nativeQuery = true)
    void insertDrone(String serial, String model, short weight, String creator, String updater);

    @Query(value = "select * from drone where serial = ?1", nativeQuery = true)
    Optional<Drone> findDroneBySerialNumber(String serial);
}
