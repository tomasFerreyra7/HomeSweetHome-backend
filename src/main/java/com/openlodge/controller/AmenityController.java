package com.openlodge.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.openlodge.dto.AmenityDTO;

import com.openlodge.service.AmenityService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/amenities")
@RequiredArgsConstructor
public class AmenityController {
    private final AmenityService amenityService;

    @GetMapping
    public ResponseEntity<List<AmenityDTO>> getAllAmenities() {
        return ResponseEntity.ok(amenityService.getAllAmenities());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AmenityDTO> getAmenityById(@PathVariable Long id) {
        return amenityService.getAmenityById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<AmenityDTO> createAmenity(@RequestBody AmenityDTO dto) {
        return ResponseEntity.ok(amenityService.createAmenity(dto));
    }
    
    @PatchMapping("/{id}")
    public ResponseEntity<AmenityDTO> updaEntity(@PathVariable Long id, @RequestBody AmenityDTO dto) {
        return ResponseEntity.ok(amenityService.updateAmenity(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAmenity(@PathVariable Long id) {
        amenityService.deleteAmenity(id);
        return ResponseEntity.noContent().build();
    }
}
