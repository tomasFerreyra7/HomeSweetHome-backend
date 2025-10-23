package com.openlodge.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.openlodge.util.ValidDateRange;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "reservations")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ValidDateRange
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "La fecha de check-in no puede ser nula")
    @FutureOrPresent(message = "La fecha de check-in debe ser hoy o una fecha futura")
    private LocalDate checkIn;

    @NotNull(message = "La fecha de check-out no puede ser nula")
    @FutureOrPresent(message = "La fecha de check-out debe ser hoy o una fecha futura")
    private LocalDate checkOut;

    @NotNull(message = "El precio total no puede ser nulo")
    @Positive(message = "El precio total debe ser un valor positivo")
    private Double totalPrice;

    @ManyToOne
    @JoinColumn(name = "guest_id")
    private User guest;

    @ManyToOne
    @JoinColumn(name = "accomodation_id")
    private Accomodation accomodation;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "payment_id")
    @JsonManagedReference
    private Payment payment;
}   
