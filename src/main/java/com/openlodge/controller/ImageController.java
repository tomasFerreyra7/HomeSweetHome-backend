package com.openlodge.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.openlodge.dto.ImageDTO;
import com.openlodge.entities.Image;
import com.openlodge.service.ImageService;

@RestController
@RequestMapping("/api/images")
public class ImageController {
    
    @Autowired
    private ImageService imageService;

    // Crear nueva image
    @PostMapping
    public ResponseEntity<Image> createImage(@RequestBody ImageDTO imageDTO) {
        Image createdImage = imageService.createImage(imageDTO);
        return ResponseEntity.status(201).body(createdImage);
    }

    // Actualizar una image
    @PatchMapping("/{id}")
    public ResponseEntity<Image> updateImage(@PathVariable Long id, @RequestBody ImageDTO imageDTO) {
        Image updatedImage = imageService.updateImage(id, imageDTO);
        return ResponseEntity.ok(updatedImage);
    }

    // Obtener todas las images de un alojamiento
    @GetMapping("/accomodation/{accomodationId}")
    public ResponseEntity<List<Image>> getImagesByAccomodation(@PathVariable Long accomodationId) {
        List<Image> images = imageService.getImagesByAccomodation(accomodationId);
        return ResponseEntity.ok(images);
    }
}
