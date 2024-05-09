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
    public ResponseEntity<?> loadDrone(@RequestParam int droneId) {
        return ResponseEntity.ok(droneService.loadDrone(droneId));
    }

    @GetMapping("/items")
    public ResponseEntity<?> getLoadedItems(@RequestParam int droneId) {
        return ResponseEntity.ok(droneService.getLoadedItems(droneId));
    }

    @GetMapping("/loading")
    public ResponseEntity<?> getLoadableDrones() {
        return ResponseEntity.ok(droneService.getLoadableDrones());
    }
}
