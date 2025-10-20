package com.openlodge.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "accomodations")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Accomodation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "El título no puede ser nulo")
    @Size(min = 5, max = 100, message = "El título debe tener entre 5 y 100 caracteres")
    private String title;

    @NotNull(message = "La descripción no puede ser nula")
    @Size(min = 10, max = 1000, message = "La descripción debe tener entre 10 y 1000 caracteres")
    private String description;

    @NotNull(message = "El precio por noche no puede ser nulo")
    @DecimalMin(value = "0.0", inclusive = false, message = "El precio debe ser mayor a 0")
    private Double pricePerNight;

    @NotNull(message = "El número máximo de huéspedes no puede ser nulo")
    @Min(value = 1, message = "Debe permitir al menos 1 huésped")
    private Integer maxGuests;

    // Relación con location 1:1
    @NotNull(message = "La ubicación no puede ser nula")
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "location_id")
    private Location location;

    // Relación con User
    @NotNull(message = "El anfitrión no puede ser nulo")
    @ManyToOne
    @JoinColumn(name = "host_id")
    private User host;

    // Relación con Amenity (N:M)
    @ManyToMany
    @JoinTable(
        name = "accomodation_amenities",
        joinColumns = @JoinColumn(name = "accomodation_id"),
        inverseJoinColumns = @JoinColumn(name = "amenity_id")
    )
    private List<Amenity> amenities;

    // Relación con Image (1:N)
    @OneToMany(mappedBy = "accomodation", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Image> images;

    // Relación con Review (1:N)
    @OneToMany(mappedBy = "accomodation", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Review> reviews;
}
