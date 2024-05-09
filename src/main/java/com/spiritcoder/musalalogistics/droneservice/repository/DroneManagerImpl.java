package com.spiritcoder.musalalogistics.droneservice.repository;

import com.spiritcoder.musalalogistics.commons.config.AppConstants;
import com.spiritcoder.musalalogistics.droneservice.entity.Drone;
import com.spiritcoder.musalalogistics.droneservice.entity.DroneMedicationBatchSnapshot;
import com.spiritcoder.musalalogistics.droneservice.enums.ModelEnum;
import com.spiritcoder.musalalogistics.droneservice.enums.StateEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DroneManagerImpl extends DroneManager {

    private static final Logger LOG = LoggerFactory.getLogger(DroneManagerImpl.class);

    private final DroneRepository droneRepository;

    private final DroneActivitySnapshotRepository droneActivitySnapshotRepository;

    private final DroneBatterySnapshotRepository droneBatterySnapshotRepository;

    private final DroneBatteryRepository droneBatteryRepository;

    private final DroneActivityRepository droneActivityRepository;

    private final DroneMedicationBatchRepository droneMedicationBatchRepository;

    private final DroneMedicationBatchSnapshotRepository droneMedicationBatchSnapshotRepository;


    @Override
    public Optional<List<Drone>> getAllInActiveDrones() {
        return droneRepository.findAllInactiveDrones();
    }

    @Override
    public boolean updateActivationStatus(boolean state, Integer id) {
        try{
            droneRepository.updateActivationStatus(state, id);

        }catch(Exception Exception){

            LOG.error(Exception.getMessage(), Exception.getCause());
            return false;
        }
        return true;
    }

    @Override
    public boolean insertDrone(String serial, ModelEnum model, short weight) {
        try{
            droneRepository.insertDrone(serial, model.toString(), weight, AppConstants.SYSTEM_USER, AppConstants.SYSTEM_USER);

        }catch(Exception Exception){

            LOG.error(Exception.getMessage(), Exception.getCause());
            return false;
        }
        return true;
    }

    @Override
    public boolean insertDroneActivitySnapshot(StateEnum stateEnum, int droneId) {
        try{
            droneActivitySnapshotRepository.insertDroneActivitySnapshot(droneId, stateEnum.toString(), AppConstants.SYSTEM_USER, AppConstants.SYSTEM_USER);

        }catch(Exception Exception){

            LOG.error(Exception.getMessage(), Exception.getCause());
            return false;
        }
        return true;
    }

    @Override
    public Optional<Drone> findDroneBySerialNumber(String serialNumber) {
        return droneRepository.findDroneBySerialNumber(serialNumber);
    }

    @Override
    public Optional<List<Drone>> findAllLoadableDrones() {
        return droneRepository.findAllLoadableDrones();
    }

    @Override
    public Optional<Drone> checkIfDroneIsLoadable(int droneId) {
        return droneRepository.checkIfDroneIsLoadable(droneId);
    }

    @Override
    public Drone getDroneById(Integer id) {

        try{
           Optional<Drone> drone =  droneRepository.findById(id);
           return drone.orElse(null);

        }catch(Exception Exception){

            LOG.error(Exception.getMessage(), Exception.getCause());
            return null;
        }
    }

    @Override
    public boolean updateDroneActivitySnapshot(String state, Integer batchId, Integer droneId) {
        try{
            droneActivitySnapshotRepository.updateDroneActivitySnapshot(state, batchId,  droneId);

        }catch (Exception Exception){

            LOG.error(Exception.getMessage(), Exception.getCause());
            return false;
        }
        return true;
    }

    @Override
    public boolean insertDroneBatterySnapshotRecord(int droneId, byte battery) {
        try{
            droneBatterySnapshotRepository.insertDroneBatterySnapshot(battery, droneId, AppConstants.SYSTEM_USER, AppConstants.SYSTEM_USER);

        }catch (Exception Exception){

            LOG.error(Exception.getMessage(), Exception.getCause());
            return false;
        }
        return true;
    }

    @Override
    public boolean insertDroneBattery(int droneId, byte batteryLevel) {
        try{
            droneBatteryRepository.insertDroneBattery(droneId, batteryLevel, AppConstants.SYSTEM_USER, AppConstants.SYSTEM_USER);

        }catch (Exception Exception){

            LOG.error(Exception.getMessage(), Exception.getCause());
            return false;
        }
        return true;
    }

    @Override
    public boolean insertDroneActivity(int droneId, String state, Integer batch) {
        try{
            droneActivityRepository.insertDroneActivity(droneId, state, batch, AppConstants.SYSTEM_USER, AppConstants.SYSTEM_USER);

        }catch (Exception Exception){

            LOG.error(Exception.getMessage(), Exception.getCause());
            return false;
        }
        return true;
    }

    @Override
    public boolean insertDroneMedicationBatchRecord(int batchId, int droneId) {
        try{
            droneMedicationBatchRepository.insertDroneMedicationBatchRecord(batchId, droneId, AppConstants.SYSTEM_USER, AppConstants.SYSTEM_USER);

        }catch (Exception Exception){

            LOG.error(Exception.getMessage(), Exception.getCause());
            return false;
        }
        return true;
    }

    @Override
    public Optional<DroneMedicationBatchSnapshot> getNextBatch() {
        try{
            return Optional.of(droneMedicationBatchSnapshotRepository.getNextBatch());

        }catch (Exception Exception){

            LOG.error(Exception.getMessage(), Exception.getCause());
            return Optional.empty();
        }
    }

    @Override
    public boolean updateDroneMedicationBatchSnapshotRecord(int batchId, int droneId) {
        try{
            droneMedicationBatchSnapshotRepository.updateDroneMedicationBatchSnapshotRepository(batchId, droneId);

        }catch (Exception Exception){

            LOG.error(Exception.getMessage(), Exception.getCause());
            return false;
        }
        return true;
    }

    @Override
    public boolean insertDroneMedicationBatchSnapshotRecord(int batchId, int droneId) {
        try{
            droneMedicationBatchSnapshotRepository.insertDroneMedicationBatchSnapshotRecord(batchId, droneId, AppConstants.SYSTEM_USER, AppConstants.SYSTEM_USER);

        }catch (Exception Exception){

            LOG.error(Exception.getMessage(), Exception.getCause());
            return false;
        }
        return true;
    }
}
