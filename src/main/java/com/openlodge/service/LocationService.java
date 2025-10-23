package com.openlodge.service;

import com.openlodge.dto.LocationDTO;
import com.openlodge.entities.Location;
import com.openlodge.repository.LocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LocationService {

    private final LocationRepository locationRepository;

    @Autowired
    public LocationService(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

    // Obtener todas las ubicaciones
    public List<Location> findAll() {
        return locationRepository.findAll();
    }

    // Obtener ubicación por ID
    public Optional<Location> findById(Long id) {
        return locationRepository.findById(id);
    }

    // Crear nueva ubicación
    public Location create(LocationDTO dto) {
        Location location = Location.builder()
                .country(dto.getCountry())
                .city(dto.getCity())
                .address(dto.getAddress())
                .postalCode(dto.getPostalCode())
                .latitude(dto.getLatitude())
                .longitude(dto.getLongitude())
                .build();

        return locationRepository.save(location);
    }

    // Actualizar ubicación existente
    public Location update(Long id, LocationDTO dto) {
        Location existingLocation = locationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ubicación no encontrada"));

        // Actualizamos solo los campos que no sean nulos
        if (dto.getCountry() != null) existingLocation.setCountry(dto.getCountry());
        if (dto.getCity() != null) existingLocation.setCity(dto.getCity());
        if (dto.getAddress() != null) existingLocation.setAddress(dto.getAddress());
        if (dto.getPostalCode() != null) existingLocation.setPostalCode(dto.getPostalCode());
        if (dto.getLatitude() != null) existingLocation.setLatitude(dto.getLatitude());
        if (dto.getLongitude() != null) existingLocation.setLongitude(dto.getLongitude());

        return locationRepository.save(existingLocation);
    }

    // Eliminar ubicación
    public void delete(Long id) {
        locationRepository.deleteById(id);
    }
}
