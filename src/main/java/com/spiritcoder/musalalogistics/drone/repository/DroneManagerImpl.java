package com.spiritcoder.musalalogistics.drone.repository;

import com.spiritcoder.musalalogistics.commons.exception.MusalaLogisticsException;
import com.spiritcoder.musalalogistics.drone.entity.Drone;
import com.spiritcoder.musalalogistics.drone.entity.DroneStateSnapshot;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DroneManagerImpl extends DroneManager {

    private static final Logger LOG = LoggerFactory.getLogger(DroneManagerImpl.class);

    private final DroneRepository droneRepository;

    private final DroneStateSnapshotRepository droneStateSnapshotRepository;

    private final DroneBatterySnapshotRepository droneBatterySnapshotRepository;

    @Override
    public Optional<List<Drone>> getAllActiveDrones() {
        return droneRepository.findAllActiveDrones();
    }

    @Override
    public Optional<List<Drone>> getAllInActiveDrones() {
        return droneRepository.findAllInactiveDrones();
    }

    @Override
    public boolean updateActivationStatus(boolean state, Integer id) {
        try{
            droneRepository.updateActivationStatus(state, id);

        }catch(MusalaLogisticsException musalaLogisticsException){

            LOG.error(musalaLogisticsException.getMessage(), musalaLogisticsException.getCause());
            return false;
        }
        return true;
    }

    @Override
    public Optional<Drone> getDroneBySerialNumber(String serialNumber) {
        return Optional.empty();
    }

    @Override
    public Drone getDroneById(Integer id) {

        try{
           Optional<Drone> drone =  droneRepository.findById(id);
           return drone.orElse(null);

        }catch(MusalaLogisticsException musalaLogisticsException){
            //TODO:: Log error to database
            LOG.error(musalaLogisticsException.getMessage(), musalaLogisticsException.getCause());
            return null;
        }
    }

    @Override
    public Optional<List<Drone>> getDroneByModelType(String model) {
        return Optional.empty();
    }

    @Override
    public Optional<List<Drone>> getDroneByWeight(Integer weight) {
        return Optional.empty();
    }

    @Override
    public boolean addDroneToDroneStateSnapshot(Integer droneId, String state) {
        try{
            droneStateSnapshotRepository.addDroneToDroneStateSnapshot(droneId, state);

        }catch (MusalaLogisticsException musalaLogisticsException){

            LOG.error(musalaLogisticsException.getMessage(), musalaLogisticsException.getCause());
            return false;
        }
        return true;
    }

    @Override
    public Optional<List<DroneStateSnapshot>> findAllLoadableDrones() {
        return droneStateSnapshotRepository.findAllLoadableDrones();
    }

    @Override
    public boolean updateDroneStateSnapshot(String name, Integer id) {
        try{
            droneStateSnapshotRepository.updateDroneStateSnapshot(name, id);

        }catch (MusalaLogisticsException musalaLogisticsException){

            LOG.error(musalaLogisticsException.getMessage(), musalaLogisticsException.getCause());
            return false;
        }
        return true;
    }

    @Override
    public boolean addDroneToDroneBatterySnapshot(int batteryLevel, int droneId) {
        try{
            droneBatterySnapshotRepository.addDroneToDroneBatterySnapshot(batteryLevel, droneId);

        }catch (MusalaLogisticsException musalaLogisticsException){

            LOG.error(musalaLogisticsException.getMessage(), musalaLogisticsException.getCause());
            return false;
        }
        return true;
    }
}
