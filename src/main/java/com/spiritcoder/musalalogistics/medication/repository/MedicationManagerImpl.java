package com.spiritcoder.musalalogistics.medication.repository;

import com.spiritcoder.musalalogistics.medication.entity.Medication;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MedicationManagerImpl extends MedicationManager{

    private final MedicationRepository medicationRepository;

    @Override
    public Optional<List<Medication>> findAllMedication() {
        return Optional.empty();
    }

    @Override
    public Optional<List<Medication>> findAllUnloadedMedication() {
        return Optional.empty();
    }

    @Override
    public Optional<List<Medication>> findAllMedicationByBatchId(int batchId) {
        return Optional.empty();
    }

    @Override
    public Optional<List<Medication>> findAllMedicationByCode(String code) {
        return Optional.empty();
    }

    @Override
    public Optional<List<Medication>> findAllMedicationByWeight(int weight) {
        return Optional.empty();
    }
}
