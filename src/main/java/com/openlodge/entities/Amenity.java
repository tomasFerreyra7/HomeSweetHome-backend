package com.openlodge.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import java.util.List;

@Entity
@Table(name = "amenities")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Amenity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "El nombre del servicio no puede ser nulo")
    @Size(min = 1, max = 100, message = "El nombre del servicio debe tener entre 1 y 100 caracteres")
    private String name;

    @ManyToMany(mappedBy = "amenities")
    private List<Accomodation> accomodations;
}
