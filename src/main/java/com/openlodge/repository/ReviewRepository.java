//Repository de review 

package com.openlodge.repository;

import com.openlodge.entities.Review;
import com.openlodge.entities.User;
import com.openlodge.entities.Accomodation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    // Buscar reseñas por usuario
    List<Review> findByUser(User user);

    // Buscar reseñas por alojamiento
    List<Review> findByAccomodation(Accomodation accomodation);

    // Buscar reseñas por fecha exacta
    List<Review> findByDate(LocalDate date);

    // Buscar reseñas por rango de fechas
    List<Review> findByDateBetween(LocalDate start, LocalDate end);

    // Buscar reseñas con determinada calificación
    List<Review> findByRating(Integer rating);
}