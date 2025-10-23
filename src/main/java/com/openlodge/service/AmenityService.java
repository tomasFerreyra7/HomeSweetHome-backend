package com.openlodge.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.openlodge.dto.AmenityDTO;
import com.openlodge.entities.Amenity;
import com.openlodge.repository.AmenityRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AmenityService {
    private final AmenityRepository amenityRepository;

    public List<AmenityDTO> getAllAmenities() {
        return amenityRepository.findAll()
         .stream()
         .map(this::convertToDto)
         .collect(Collectors.toList());
    }

    public Optional<AmenityDTO> getAmenityById(Long id) {
        return amenityRepository.findById(id)
         .map(this::convertToDto);
    }

    public AmenityDTO createAmenity(AmenityDTO dto) {
        Amenity amenity = Amenity.builder()
         .name(dto.getName())
         .build();
        Amenity saved = amenityRepository.save(amenity);
        return convertToDto(saved);
    }

    public AmenityDTO updateAmenity(Long id, AmenityDTO dto) {
        Amenity amenity = amenityRepository.findById(id).orElseThrow(() -> new RuntimeException("Amenity not found"));
        amenity.setName(dto.getName());
        Amenity updated = amenityRepository.save(amenity);
        return convertToDto(updated);
    }

    public void deleteAmenity(Long id) {
        amenityRepository.deleteById(id);
    }

    private AmenityDTO convertToDto(Amenity amenity) {
        return AmenityDTO.builder().id(amenity.getId()).name(amenity.getName()).build();
    }
}
