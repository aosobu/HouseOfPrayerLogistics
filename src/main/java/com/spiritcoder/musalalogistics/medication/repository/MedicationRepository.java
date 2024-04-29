package com.spiritcoder.musalalogistics.medication.repository;

import com.spiritcoder.musalalogistics.medication.entity.Medication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MedicationRepository extends JpaRepository<Medication, Integer> {
}
