//Service de review

package com.openlodge.service;

import com.openlodge.entities.Review;
import com.openlodge.entities.User;
import com.openlodge.entities.Accomodation;
import com.openlodge.repository.ReviewRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;

    public ReviewService(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    // Crear una reseña
    public Review createReview(Review review) {
        return reviewRepository.save(review);
    }

    // Obtener todas las reseñas
    public List<Review> getAllReviews() {
        return reviewRepository.findAll();
    }

    // Obtener reseña por ID
    public Optional<Review> getReviewById(Long id) {
        return reviewRepository.findById(id);
    }

    // Obtener reseñas por usuario
    public List<Review> getReviewsByUser(User user) {
        return reviewRepository.findByUser(user);
    }

    // Obtener reseñas por alojamiento
    public List<Review> getReviewsByAccomodation(Accomodation accomodation) {
        return reviewRepository.findByAccomodation(accomodation);
    }

    // Obtener reseñas por fecha
    public List<Review> getReviewsByDate(LocalDate date) {
        return reviewRepository.findByDate(date);
    }

    // Obtener reseñas por rango de fechas
    public List<Review> getReviewsBetweenDates(LocalDate start, LocalDate end) {
        return reviewRepository.findByDateBetween(start, end);
    }

    // Obtener reseñas por calificación
    public List<Review> getReviewsByRating(Integer rating) {
        return reviewRepository.findByRating(rating);
    }
}