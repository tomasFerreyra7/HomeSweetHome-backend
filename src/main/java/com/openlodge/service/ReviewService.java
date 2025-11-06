package com.openlodge.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.openlodge.dto.ReviewDTO;
import com.openlodge.entities.Accomodation;
import com.openlodge.entities.Review;
import com.openlodge.entities.User;
import com.openlodge.repository.AccomodationRepository;
import com.openlodge.repository.ReviewRepository;
import com.openlodge.repository.UserRepository;
import com.openlodge.util.ResourceNotFoundException;

import jakarta.transaction.Transactional;


@Service
@Transactional
public class ReviewService {
    
    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AccomodationRepository accomodationRepository;

    public Review creatReview(ReviewDTO reviewDTO) {
        User user = userRepository.findById(reviewDTO.getUserId())
            .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con id: " + reviewDTO.getUserId()));

        Accomodation accomodation = accomodationRepository.findById(reviewDTO.getAccomodationId())
            .orElseThrow(() -> new ResourceNotFoundException("Alojamiento no encontrado con id: " + reviewDTO.getAccomodationId()));

        // Crear y guardar la reseña
        Review review = Review.builder()
                .rating(reviewDTO.getRating())
                .comment(reviewDTO.getComment())
                .date(reviewDTO.getDate())
                .user(user)
                .accomodation(accomodation)
                .build();

        return reviewRepository.save(review);
    }

    public List<Review> getAllReviews() {
        return reviewRepository.findAll();
    }

    public Review getReviewById(Long id) {
        return reviewRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Review no encontrada con id: " + id));
    }

    public Review updatReview(Long id, ReviewDTO reviewDTO) {
        // Obtener la reseña existente por su ID
        Review existingReview = getReviewById(id);
    
        // Solo actualizamos los campos que no son nulos
        if (reviewDTO.getRating() != null) {
            existingReview.setRating(reviewDTO.getRating());
        }
        if (reviewDTO.getComment() != null) {
            existingReview.setComment(reviewDTO.getComment());
        }
        if (reviewDTO.getDate() != null) {
            existingReview.setDate(reviewDTO.getDate());
        }
    
        // Si se pasa un userId, buscamos el usuario y lo asignamos
        if (reviewDTO.getUserId() != null) {
            User user = userRepository.findById(reviewDTO.getUserId())
                    .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con id: " + reviewDTO.getUserId()));
            existingReview.setUser(user);
        }
    
        // Si se pasa un accomodationId, buscamos el alojamiento y lo asignamos
        if (reviewDTO.getAccomodationId() != null) {
            Accomodation accomodation = accomodationRepository.findById(reviewDTO.getAccomodationId())
                    .orElseThrow(() -> new ResourceNotFoundException("Alojamiento no encontrado con id: " + reviewDTO.getAccomodationId()));
            existingReview.setAccomodation(accomodation);
        }
    
        // Guardamos la reseña actualizada
        return reviewRepository.save(existingReview);
    }

    public void deleteReview(Long id) {
        Review existingReview = getReviewById(id);
        reviewRepository.delete(existingReview);
    }
}
