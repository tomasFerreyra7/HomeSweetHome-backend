package com.openlodge.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Entity
@Table(name = "images")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "La URL de la imagen no puede ser nula")
    @Size(min = 5, max = 500, message = "La URL debe tener entre 5 y 500 caracteres")
    private String url;

    @ManyToOne
    @JoinColumn(name = "accomodation_id")
    private Accomodation accomodation;
}
