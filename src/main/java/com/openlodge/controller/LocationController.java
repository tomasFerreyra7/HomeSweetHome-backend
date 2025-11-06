package com.openlodge.controller;

import com.openlodge.dto.LocationDTO;
import com.openlodge.entities.Location;
import com.openlodge.service.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/locations")
public class LocationController {

    private final LocationService locationService;

    @Autowired
    public LocationController(LocationService locationService) {
        this.locationService = locationService;
    }

    // Obtener todas las ubicaciones
    @GetMapping
    public List<Location> getAllLocations() {
        return locationService.findAll();
    }

    // Obtener ubicación por ID
    @GetMapping("/{id}")
    public ResponseEntity<Location> getLocationById(@PathVariable Long id) {
        Optional<Location> location = locationService.findById(id);
        return location.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Crear nueva ubicación
    @PostMapping
    public ResponseEntity<Location> createLocation(@RequestBody @Validated LocationDTO locationDTO) {
        Location createdLocation = locationService.create(locationDTO);
        return ResponseEntity.status(201).body(createdLocation);
    }

    // Actualizar ubicación existente
    @PatchMapping("/{id}")
    public ResponseEntity<Location> updateLocation(@PathVariable Long id, @RequestBody @Validated LocationDTO locationDTO) {
        try {
            Location updatedLocation = locationService.update(id, locationDTO);
            return ResponseEntity.ok(updatedLocation);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Eliminar ubicación
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLocation(@PathVariable Long id) {
        locationService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
