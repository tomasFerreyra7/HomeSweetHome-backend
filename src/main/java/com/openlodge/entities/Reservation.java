package com.openlodge.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "reservations")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "check_in", nullable = false)
    private LocalDate checkIn;

    @Column(name = "check_out", nullable = false)
    private LocalDate checkOut;

    @Column(nullable = false)
    private Double totalPrice;

    // --- RELACIÓN CON EL HUÉSPED ---
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "guest_id")
    // Ignoramos "reservations" dentro del usuario para no hacer bucle
    // Ignoramos el "handler" de hibernate para evitar error 500 por Lazy Loading
    @JsonIgnoreProperties({"reservations", "password", "hibernateLazyInitializer", "handler"}) 
    private User guest;

    // --- RELACIÓN CON EL ALOJAMIENTO ---
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "accomodation_id")
    // Importante: Ignoramos la lista de reservas dentro del alojamiento para no volver aquí
    @JsonIgnoreProperties({"reservations", "reviews", "hibernateLazyInitializer", "handler"})
    private Accomodation accomodation;

    // --- RELACIÓN CON PAGO ---
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "payment_id", referencedColumnName = "id")
    private Payment payment;

    @Column(name = "deleted_at")
    private LocalDate deletedAt;
}
