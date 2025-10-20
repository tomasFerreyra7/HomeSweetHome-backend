package com.openlodge.entity;

import jakarta.persistence.*;
import lombok.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.DecimalMax;


@Entity
@Table(name = "locations")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Location {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "El país no puede ser nulo")
    @Size(min = 1, max = 100, message = "El país debe tener entre 1 y 100 caracteres")
    private String country;
    
    @NotNull(message = "La ciudad no puede ser nula")
    @Size(min = 1, max = 100, message = "La ciudad debe tener entre 1 y 100 caracteres")
    private String city;

    @NotNull(message = "La dirección no puede ser nula")
    @Size(min = 1, max = 200, message = "La dirección debe tener entre 1 y 200 caracteres")
    private String address;

    @NotNull(message = "El código postal no puede ser nulo")
    @Pattern(regexp = "^[0-9]{5}$", message = "El código postal debe tener 5 dígitos")
    private String postalCode;

    @DecimalMin(value = "-90.0", message = "La latitud debe estar entre -90 y 90")
    @DecimalMax(value = "90.0", message = "La latitud debe estar entre -90 y 90")
    private Double latitude;

    @DecimalMin(value = "-180.0", message = "La longitud debe estar entre -180 y 180")
    @DecimalMax(value = "180.0", message = "La longitud debe estar entre -180 y 180")
    private Double longitude;
}
