package com.spiritcoder.musalalogistics.drone.repository;

import com.spiritcoder.musalalogistics.drone.entity.Drone;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DroneRepositoryManagerImpl implements DroneRepositoryManager{

    private final DroneRepository droneRepository;

    @Override
    public Optional<List<Drone>> getAllActiveDrones() {
        return droneRepository.findAllActiveDrones();
    }

    @Override
    public Optional<List<Drone>> getAllInActiveDrones() {
        return droneRepository.findAllInactiveDrones();
    }

    @Override
    public Optional<Drone> getDroneBySerialNumber(String serialNumber) {
        return Optional.empty();
    }

    @Override
    public Optional<Drone> getDroneById(Integer id) {
        return Optional.empty();
    }

    @Override
    public Optional<List<Drone>> getDroneByModelType(String model) {
        return Optional.empty();
    }

    @Override
    public Optional<List<Drone>> getDroneByWeight(Integer weight) {
        return Optional.empty();
    }
}
