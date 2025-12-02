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
        // 1. Host
        User host = userRepository.findById(dto.getHostId())
                .orElseThrow(() -> new RuntimeException("Host no encontrado"));

        // 2. Amenities (Usando getAmenityIds)
        List<Amenity> amenities = new ArrayList<>();
        if (dto.getAmenityIds() != null && !dto.getAmenityIds().isEmpty()) {
            amenities = amenityRepository.findAllById(dto.getAmenityIds());
        }

        // 3. Crear Objeto
        Accomodation accomodation = new Accomodation();
        accomodation.setTitle(dto.getTitle());
        accomodation.setDescription(dto.getDescription());
        accomodation.setPricePerNight(dto.getPricePerNight());
        accomodation.setMaxGuests(dto.getMaxGuests());
        accomodation.setHost(host);
        accomodation.setAmenities(amenities);

        // 4. Location
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

        // 5. Imágenes
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

        if (dto.getTitle() != null) existing.setTitle(dto.getTitle());
        if (dto.getDescription() != null) existing.setDescription(dto.getDescription());
        if (dto.getPricePerNight() != null) existing.setPricePerNight(dto.getPricePerNight());
        if (dto.getMaxGuests() != null) existing.setMaxGuests(dto.getMaxGuests());

        // Update Location
        if (dto.getLocation() != null) {
            Location loc = existing.getLocation();
            if (loc == null) {
                loc = new Location();
                existing.setLocation(loc);
            }
            if(dto.getLocation().getCity() != null) loc.setCity(dto.getLocation().getCity());
            if(dto.getLocation().getCountry() != null) loc.setCountry(dto.getLocation().getCountry());
            if(dto.getLocation().getAddress() != null) loc.setAddress(dto.getLocation().getAddress());
            if(dto.getLocation().getPostalCode() != null) loc.setPostalCode(dto.getLocation().getPostalCode());
        }

        // Update Amenities (CORREGIDO AQUÍ)
        // Usamos getAmenityIds() porque eso es lo que tiene tu DTO
        if (dto.getAmenityIds() != null) {
             List<Amenity> newAmenities = amenityRepository.findAllById(dto.getAmenityIds());
             existing.setAmenities(newAmenities);
        }

        // Update Images (Reemplazo total)
        if (dto.getImages() != null) {
            if (existing.getImages() != null) existing.getImages().clear();
            else existing.setImages(new ArrayList<>());

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
        // 1. Verificar existencia
        Accomodation accomodation = accomodationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Alojamiento no encontrado con ID: " + id));

        // 2. Borrar RESERVAS asociadas primero
        // Esto evita el error de "Foreign Key Constraint"
        List<Reservation> reservasAsociadas = reservationRepository.findByAccomodationId(id);
        
        if (reservasAsociadas != null && !reservasAsociadas.isEmpty()) {
            reservationRepository.deleteAll(reservasAsociadas);
        }

        // 3. Borrar el Alojamiento (Cascade borra imágenes, location y reviews)
        accomodationRepository.delete(accomodation);
    }
}
