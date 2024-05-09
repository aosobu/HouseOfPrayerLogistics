package com.spiritcoder.musalalogistics.droneservice.repository;

public interface DroneMedicationBatchSnapshotManager {

    boolean updateDroneMedicationBatchSnapshotRecord(int batchId, int droneId);

    boolean insertDroneMedicationBatchSnapshotRecord(int batchId, int droneId);
}
