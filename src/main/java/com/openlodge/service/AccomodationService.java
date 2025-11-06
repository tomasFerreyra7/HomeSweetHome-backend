package com.openlodge.service;

import java.util.List;
import java.util.Optional;

import com.openlodge.entities.User;
import org.springframework.stereotype.Service;

import com.openlodge.dto.AccomodationDTO;
import com.openlodge.entities.Accomodation;
import com.openlodge.entities.Amenity;
import com.openlodge.entities.Location;
import com.openlodge.repository.AccomodationRepository;
import com.openlodge.repository.AmenityRepository;
import com.openlodge.repository.LocationRepository;
import com.openlodge.repository.UserRepository;

@Service
public class AccomodationService {
    private final AccomodationRepository accomodationRepository;
    private final UserRepository userRepository;
    private final LocationRepository locationRepository;
    private final AmenityRepository amenityRepository;

    public AccomodationService(
            AccomodationRepository accomodationRepository,
            UserRepository userRepository,
            LocationRepository locationRepository,
            AmenityRepository amenityRepository
    ) {
        this.accomodationRepository = accomodationRepository;
        this.userRepository = userRepository;
        this.locationRepository = locationRepository;
        this.amenityRepository = amenityRepository;
    }

    public List<Accomodation> findAll() {
        return accomodationRepository.findAll();
    }

    public Optional<Accomodation> findById(Long id) {
        return accomodationRepository.findById(id);
    }

    public Accomodation create(AccomodationDTO dto) {
        User host = (User) userRepository.findById(dto.getHostId())
                .orElseThrow(() -> new RuntimeException("Host no encontrado"));

        Location location = locationRepository.findById(dto.getLocationId())
                .orElseThrow(() -> new RuntimeException("Ubicación no encontrada"));

        List<Amenity> amenities = amenityRepository.findAllById(dto.getAmenityIds());

        Accomodation accomodation = Accomodation.builder()
                .title(dto.getTitle())
                .description(dto.getDescription())
                .pricePerNight(dto.getPricePerNight())
                .maxGuests(dto.getMaxGuests())
                .host(host)
                .location(location)
                .amenities(amenities)
                .build();

        return accomodationRepository.save(accomodation);
    }

    public Accomodation update(Long id, AccomodationDTO dto) {
        Accomodation existing = accomodationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Alojamiento no encontrado"));

        if (dto.getTitle() != null) existing.setTitle(dto.getTitle());
        if (dto.getDescription() != null) existing.setDescription(dto.getDescription());
        if (dto.getPricePerNight() != null) existing.setPricePerNight(dto.getPricePerNight());
        if (dto.getMaxGuests() != null) existing.setMaxGuests(dto.getMaxGuests());

        if (dto.getHostId() != null) {
            User host = userRepository.findById(dto.getHostId())
                    .orElseThrow(() -> new RuntimeException("Host no encontrado"));
            existing.setHost(host);
        }

        if (dto.getLocationId() != null) {
            Location location = locationRepository.findById(dto.getLocationId())
                    .orElseThrow(() -> new RuntimeException("Ubicación no encontrada"));
            existing.setLocation(location);
        }

        if (dto.getAmenityIds() != null && !dto.getAmenityIds().isEmpty()) {
            List<Amenity> amenities = amenityRepository.findAllById(dto.getAmenityIds());
            existing.setAmenities(amenities);
        }

        return accomodationRepository.save(existing);
    }

    public void delete(Long id) {
        accomodationRepository.deleteById(id);
    }
}
