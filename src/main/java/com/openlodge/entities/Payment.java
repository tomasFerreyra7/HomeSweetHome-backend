package com.openlodge.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Entity
@Table(name = "payments")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "El método de pago no puede ser nulo")
    @Size(min = 3, max = 50, message = "El método de pago debe tener entre 3 y 50 caracteres")
    private String method; // tarjeta, transferencia, etc.

    @NotNull(message = "El monto no puede ser nulo")
    @Positive(message = "El monto debe ser un número positivo")
    private Double amount;

    @NotNull(message = "El estado del pago no puede ser nulo")
    @Pattern(
        regexp = "^(pending|completed|refunded|failed)$",
        message = "El estado debe ser: pending, completed, refunded o failed"
    )
    private String status; // pending, completed, refunded, etc.

    @OneToOne
    @JoinColumn(name = "reservation_id")
    private Reservation reservation;
}
