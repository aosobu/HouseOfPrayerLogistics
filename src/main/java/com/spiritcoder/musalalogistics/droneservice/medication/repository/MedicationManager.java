package com.spiritcoder.musalalogistics.droneservice.medication.repository;


import com.spiritcoder.musalalogistics.droneservice.medication.entity.Medication;

import java.util.List;
import java.util.Optional;

public abstract class MedicationManager {

    public abstract Optional<List<Medication>> findAllLoadableMedication();

    public abstract Optional<List<Medication>> findAllMedicationByBatchId(int batchId);

    public abstract void updateMedicationRecordWithBatchId(int medicationId, int batchId);

    public abstract Optional<List<Medication>> getLoadedItems(int droneId);
}
