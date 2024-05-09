package com.spiritcoder.musalalogistics.droneservice.repository;

import com.spiritcoder.musalalogistics.droneservice.entity.DroneMedicationBatch;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface DroneMedicationBatchRepository extends JpaRepository<DroneMedicationBatch, Integer> {

    @Modifying
    @Transactional
    @Query(value = "insert into DroneMedicationBatch(batch, drone, creator, updater, created) values (?1, ?2, ?3, ?4, getDate())", nativeQuery = true)
    void insertDroneMedicationBatchRecord (int batchId, int droneId, String creator, String updater);
}
