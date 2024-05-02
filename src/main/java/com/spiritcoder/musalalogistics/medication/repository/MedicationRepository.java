package com.spiritcoder.musalalogistics.medication.repository;

import com.spiritcoder.musalalogistics.medication.entity.Medication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MedicationRepository extends JpaRepository<Medication, Integer> {

    @Query(value = "select * from medicationTable where delivered = 0", nativeQuery = true)
    List<Medication> findAllLoadableMedication();
}
