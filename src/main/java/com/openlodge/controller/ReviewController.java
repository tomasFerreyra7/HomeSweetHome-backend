//Controller de peticiones del Review

package com.openlodge.controller;

import com.openlodge.entities.Review;
import com.openlodge.entities.User;
import com.openlodge.entities.Accomodation;
import com.openlodge.service.ReviewService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.*;
import java.util.Optional;

@RestController
@RequestMapping("/api/review")
@CrossOrigin(origins = "*")
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    // Crear una reseña
    @PostMapping
    public ResponseEntity<Review> createReview(@Valid @RequestBody Review review) {
        Review created = reviewService.createReview(review);
        return ResponseEntity.ok(created);
    }

    // Obtener todas las reseñas
    @GetMapping
    public List<Review> getAllReviews() {
        return reviewService.getAllReviews();
    }

    // Obtener reseña por ID
    @GetMapping("/{id}")
    public ResponseEntity<Review> getReviewById(@PathVariable Long id) {
        Optional<Review> review = reviewService.getReviewById(id);
        return review.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    // Obtener reseñas por usuario
    @GetMapping("/user/{userId}")
    public List<Review> getReviewsByUser(@PathVariable Long userId) {
        User user = new User();
        user.setId(userId);
        return reviewService.getReviewsByUser(user);
    }

    // Obtener reseñas por alojamiento
    @GetMapping("/accomodation/{accomodationId}")
    public List<Review> getReviewsByAccomodation(@PathVariable Long accomodationId) {
        Accomodation acc = new Accomodation();
        acc.setId(accomodationId);
        return reviewService.getReviewsByAccomodation(acc);
    }

    // Obtener reseñas por fecha
    @GetMapping("/date")
    public List<Review> getReviewsByDate(@RequestParam LocalDate date) {
        return reviewService.getReviewsByDate(date);
    }

    // Obtener reseñas por calificación
    @GetMapping("/rating")
    public List<Review> getReviewsByRating(@RequestParam Integer rating) {
        return reviewService.getReviewsByRating(rating);
    }
}