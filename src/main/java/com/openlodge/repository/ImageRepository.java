package com.openlodge.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.openlodge.entities.Accomodation;
import com.openlodge.entities.Image;

public interface ImageRepository extends JpaRepository<Image, Long> {

    // Método para encontrar imágenes por alojamiento
    List<Image> findByAccomodation(Accomodation accomodation);
    
}
