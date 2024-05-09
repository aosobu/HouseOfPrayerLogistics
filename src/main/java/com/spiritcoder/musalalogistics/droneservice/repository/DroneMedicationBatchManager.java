package com.spiritcoder.musalalogistics.droneservice.repository;

import com.spiritcoder.musalalogistics.droneservice.entity.DroneMedicationBatchSnapshot;

import java.util.Optional;

public interface DroneMedicationBatchManager {

    boolean insertDroneMedicationBatchRecord (int batchId, int droneId);

    Optional<DroneMedicationBatchSnapshot>  getNextBatch();
}
