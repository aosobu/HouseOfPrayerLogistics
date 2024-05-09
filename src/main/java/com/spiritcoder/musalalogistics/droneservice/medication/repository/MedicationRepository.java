package com.spiritcoder.musalalogistics.droneservice.medication.repository;

import com.spiritcoder.musalalogistics.droneservice.medication.entity.Medication;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MedicationRepository extends JpaRepository<Medication, Integer> {

    @Query(value = "select * from Medication where batch is null and delivered = 0", nativeQuery = true)
    List<Medication> findAllLoadableMedication();

    @Query(value = "select * from Medication where batch = ?1", nativeQuery = true)
    List<Medication> findAllMedicationByBatch(int batchId);

    @Modifying
    @Transactional
    @Query(value = "update Medication set batch = ?2 where id = ?1 ", nativeQuery = true)
    void updateMedicationRecordWithBatchId(int medicationId, int batchId);
}
