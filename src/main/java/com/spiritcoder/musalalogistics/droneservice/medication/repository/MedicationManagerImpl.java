package com.spiritcoder.musalalogistics.droneservice.medication.repository;

import com.spiritcoder.musalalogistics.droneservice.medication.entity.Medication;
import com.spiritcoder.musalalogistics.droneservice.service.component.DroneLoaderComponent;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MedicationManagerImpl extends MedicationManager{

    private static final Logger LOG = LoggerFactory.getLogger(MedicationManagerImpl.class);

    private final MedicationRepository medicationRepository;

    @Override
    public Optional<List<Medication>> findAllLoadableMedication() {
        try{
            List<Medication> medicationList = medicationRepository.findAllLoadableMedication();
            return Optional.of(medicationList);
        }catch(Exception exception){
            throw new RuntimeException(exception.getMessage());
        }
    }

    @Override
    public Optional<List<Medication>> findAllMedicationByBatchId(int batchId) {
        try{
            return Optional.of(medicationRepository.findAllMedicationByBatch(batchId));
        }catch(Exception exception){
            return Optional.empty();
        }
    }

    @Override
    public void updateMedicationRecordWithBatchId(int medicationId, int batchId) {
        try{
            medicationRepository.updateMedicationRecordWithBatchId(medicationId,batchId);
        }catch (Exception exception){
            LOG.error(exception.getMessage());
        }
    }

    @Override
    public Optional<List<Medication>> getLoadedItems(int droneId) {
        try{
            return Optional.of(medicationRepository.findAllItemsByDroneId(droneId));
        }catch(Exception exception){
            return Optional.empty();
        }
    }
}
