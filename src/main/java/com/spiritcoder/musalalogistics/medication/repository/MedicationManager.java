package com.spiritcoder.musalalogistics.medication.repository;


import com.spiritcoder.musalalogistics.medication.entity.Medication;

import java.util.List;
import java.util.Optional;

public abstract class MedicationManager {

    public abstract Optional<List<Medication>> findAllMedication();

    public abstract Optional<List<Medication>> findAllUnloadedMedication();

    public abstract Optional<List<Medication>> findAllMedicationByBatchId(int batchId);

    public abstract Optional<List<Medication>> findAllMedicationByCode(String code);

    public abstract Optional<List<Medication>> findAllMedicationByWeight(int weight);
}
