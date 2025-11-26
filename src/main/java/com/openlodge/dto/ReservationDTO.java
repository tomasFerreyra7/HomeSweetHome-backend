package com.openlodge.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReservationDTO {

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

    @NotNull(message = "El ID del huésped es obligatorio")
    private Long guestId;

    @NotNull(message = "El ID del alojamiento es obligatorio")
    private Long accomodationId;

    private Long paymentId;
}
