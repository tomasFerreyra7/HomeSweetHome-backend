package com.openlodge.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.openlodge.dto.ImageDTO;
import com.openlodge.entities.Accomodation;
import com.openlodge.entities.Image;
import com.openlodge.repository.AccomodationRepository;
import com.openlodge.repository.ImageRepository;
import com.openlodge.util.ResourceNotFoundException;

@Service
public class ImageService {
    
    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private AccomodationRepository accomodationRepository;

    // Crear una nueva imagen
    public Image createImage(ImageDTO imageDTO) {
        Accomodation accomodation = accomodationRepository.findById(imageDTO.getAccomodationId()).orElseThrow(() -> new ResourceNotFoundException("Alojamiento no encontrado con el id: " + imageDTO.getAccomodationId()));

        Image image = Image.builder()
            .url(imageDTO.getUrl())
            .accomodation(accomodation)
            .build();

        return imageRepository.save(image);
    }

    // Actualizar una image 
    public Image updateImage(Long id, ImageDTO imageDTO) {
        Image existingImage = imageRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Imagen no encontrada con id: " + id));

        if (imageDTO.getUrl() != null) {
            existingImage.setUrl(imageDTO.getUrl());
        }
        if (imageDTO.getAccomodationId() != null) {
            Accomodation accomodation = accomodationRepository.findById(imageDTO.getAccomodationId())
                .orElseThrow(() -> new ResourceNotFoundException("Alojamiento no encontrado con id: " + imageDTO.getAccomodationId()));
            existingImage.setAccomodation(accomodation);
        }
        return imageRepository.save(existingImage);        
    }

    // Obtener todas las images
    public List<Image> getImagesByAccomodation(Long accomodationId) {
        // Buscar el alojamiento por ID
        Accomodation accomodation = accomodationRepository.findById(accomodationId)
                .orElseThrow(() -> new ResourceNotFoundException("Alojamiento no encontrado con id: " + accomodationId));
    
        // Buscar y devolver las imágenes asociadas a este alojamiento
        return imageRepository.findByAccomodation(accomodation);  // Cambié el método a findByAccomodation
    }
}
