package com.spiritcoder.musalalogistics.drone.repository;

import com.spiritcoder.musalalogistics.drone.entity.Drone;
import org.springframework.data.jpa.repository.JpaRepository;
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
}
