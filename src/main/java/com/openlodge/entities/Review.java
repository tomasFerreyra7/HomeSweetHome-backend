package com.openlodge.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "reviews")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "La calificación no puede ser nula")
    @Min(value = 1, message = "La calificación mínima es 1")
    @Max(value = 5, message = "La calificación máxima es 5")
    private Integer rating;

    @NotNull(message = "El comentario no puede ser nulo")
    @Size(min = 5, max = 1000, message = "El comentario debe tener entre 5 y 1000 caracteres")
    private String comment;

    @NotNull(message = "La fecha de la reseña no puede ser nula")
    @PastOrPresent(message = "La fecha debe ser en el pasado o presente")
    private LocalDate date;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "accomodation_id")
    @JsonBackReference
    private Accomodation accomodation;
}
