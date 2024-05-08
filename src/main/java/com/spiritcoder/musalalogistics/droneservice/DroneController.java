package com.spiritcoder.musalalogistics.droneservice;

import com.spiritcoder.musalalogistics.droneservice.api.DroneRequest;
import com.spiritcoder.musalalogistics.droneservice.service.DroneService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/drone")
@RequiredArgsConstructor
public class DroneController {

    private final DroneService droneService;

    @PostMapping
    public ResponseEntity<?> register(@Valid @RequestBody DroneRequest droneRequest) {
        return ResponseEntity.ok(droneService.registerDrone(droneRequest));
    }

    @PostMapping("/load")
    public ResponseEntity<?> loadDrone(@RequestParam String serialNumber) {
        return ResponseEntity.ok(droneService.loadDrone(serialNumber));
    }

    @GetMapping("/items")
    public ResponseEntity<?> getLoadedItems(@RequestParam String serialNumber) {
        return ResponseEntity.ok(droneService.loadDrone(serialNumber));
    }
}
