package com.spiritcoder.musalalogistics.droneservice.repository;

import com.spiritcoder.musalalogistics.droneservice.entity.DroneMedicationBatchSnapshot;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DroneMedicationBatchSnapshotRepository extends JpaRepository<DroneMedicationBatchSnapshot, Integer> {

    @Modifying
    @Transactional
    @Query(value = "update DroneMedicationBatchSnapshot set batch = ?1 where drone = ?2", nativeQuery = true)
    void updateDroneMedicationBatchSnapshotRepository(int batchId, int droneId);

    @Modifying
    @Transactional
    @Query(value = "insert into DroneMedicationBatchSnapshot(batch, drone, creator, updater, created) values (?1, ?2, ?3, ?4, getDate())", nativeQuery = true)
    void insertDroneMedicationBatchSnapshotRecord(int batchId, int droneId, String creator, String updater);

    @Query(value = "select Top(1) * from DroneMedicationBatchSnapshot order by batch desc", nativeQuery = true)
    DroneMedicationBatchSnapshot getNextBatch();
}
