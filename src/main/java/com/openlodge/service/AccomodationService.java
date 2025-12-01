package com.openlodge.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.openlodge.dto.AccomodationDTO;
import com.openlodge.dto.ImageDTO;
import com.openlodge.entities.Accomodation;
import com.openlodge.entities.Amenity;
import com.openlodge.entities.Image;
import com.openlodge.entities.Location;
import com.openlodge.entities.Reservation;
import com.openlodge.entities.User;
import com.openlodge.repository.AccomodationRepository;
import com.openlodge.repository.AmenityRepository;
import com.openlodge.repository.ReservationRepository;
import com.openlodge.repository.UserRepository;

@Service
public class AccomodationService {

    private final AccomodationRepository accomodationRepository;
    private final UserRepository userRepository;
    private final AmenityRepository amenityRepository;
    private final ReservationRepository reservationRepository;

    public AccomodationService(
            AccomodationRepository accomodationRepository,
            UserRepository userRepository,
            AmenityRepository amenityRepository,
            ReservationRepository reservationRepository
    ) {
        this.accomodationRepository = accomodationRepository;
        this.userRepository = userRepository;
        this.amenityRepository = amenityRepository;
        this.reservationRepository = reservationRepository;
    }

    public List<Accomodation> findAll() {
        return accomodationRepository.findAll();
    }

    public Optional<Accomodation> findById(Long id) {
        return accomodationRepository.findById(id);
    }

    @Transactional
    public Accomodation create(AccomodationDTO dto) {
        User host = userRepository.findById(dto.getHostId())
                .orElseThrow(() -> new RuntimeException("Host no encontrado"));

        List<Amenity> amenities = new ArrayList<>();
        if (dto.getAmenityIds() != null && !dto.getAmenityIds().isEmpty()) {
            amenities = amenityRepository.findAllById(dto.getAmenityIds());
        }

        Accomodation accomodation = Accomodation.builder()
                .title(dto.getTitle())
                .description(dto.getDescription())
                .pricePerNight(dto.getPricePerNight())
                .maxGuests(dto.getMaxGuests())
                .host(host)
                .amenities(amenities)
                .build();

        if (dto.getLocation() != null) {
            Location newLocation = new Location();
            newLocation.setCity(dto.getLocation().getCity());
            newLocation.setCountry(dto.getLocation().getCountry());
            newLocation.setAddress(dto.getLocation().getAddress());
            newLocation.setPostalCode(dto.getLocation().getPostalCode());
            newLocation.setLatitude(dto.getLocation().getLatitude());
            newLocation.setLongitude(dto.getLocation().getLongitude());
            accomodation.setLocation(newLocation);
        }

        List<Image> imageList = new ArrayList<>();
        if (dto.getImages() != null) {
            for (ImageDTO imgDto : dto.getImages()) {
                Image image = new Image();
                image.setUrl(imgDto.getUrl());
                image.setAccomodation(accomodation);
                imageList.add(image);
            }
        }
        accomodation.setImages(imageList);

        return accomodationRepository.save(accomodation);
    }

    @Transactional
    public Accomodation update(Long id, AccomodationDTO dto) {
        Accomodation existing = accomodationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Alojamiento no encontrado"));

        // 1. Campos Simples
        if (dto.getTitle() != null) existing.setTitle(dto.getTitle());
        if (dto.getDescription() != null) existing.setDescription(dto.getDescription());
        if (dto.getPricePerNight() != null) existing.setPricePerNight(dto.getPricePerNight());
        if (dto.getMaxGuests() != null) existing.setMaxGuests(dto.getMaxGuests());

        // 2. Actualizar HOST (NUEVO: Esto es lo que faltaba)
        if (dto.getHostId() != null) {
            // Solo buscamos en BD si el ID es diferente al actual
            if (!existing.getHost().getId().equals(dto.getHostId())) {
                User newHost = userRepository.findById(dto.getHostId())
                        .orElseThrow(() -> new RuntimeException("Nuevo Host no encontrado"));
                existing.setHost(newHost);
            }
        }

        // 3. Actualizar Location
        if (dto.getLocation() != null) {
            Location loc = existing.getLocation();
            if (loc == null) {
                loc = new Location();
                existing.setLocation(loc);
            }
            if (dto.getLocation().getCity() != null) loc.setCity(dto.getLocation().getCity());
            if (dto.getLocation().getCountry() != null) loc.setCountry(dto.getLocation().getCountry());
            if (dto.getLocation().getAddress() != null) loc.setAddress(dto.getLocation().getAddress());
            if (dto.getLocation().getPostalCode() != null) loc.setPostalCode(dto.getLocation().getPostalCode());
        }

        // 4. Actualizar Amenities
        if (dto.getAmenityIds() != null) {
            List<Amenity> amenities = amenityRepository.findAllById(dto.getAmenityIds());
            existing.setAmenities(amenities);
        }

        // 5. Actualizar Imágenes
        if (dto.getImages() != null) {
            if (existing.getImages() != null) {
                existing.getImages().clear();
            } else {
                existing.setImages(new ArrayList<>());
            }

            for (ImageDTO imgDto : dto.getImages()) {
                Image img = new Image();
                img.setUrl(imgDto.getUrl());
                img.setAccomodation(existing);
                existing.getImages().add(img);
            }
        }

        return accomodationRepository.save(existing);
    }

    @Transactional
    public void delete(Long id) {
        Accomodation accomodation = accomodationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Alojamiento no encontrado"));

        // Borrar reservas asociadas primero para evitar error de FK
        List<Reservation> reservasAsociadas = reservationRepository.findByAccomodationId(id);
        if (!reservasAsociadas.isEmpty()) {
            reservationRepository.deleteAll(reservasAsociadas);
        }

        accomodationRepository.delete(accomodation);
    }
}
