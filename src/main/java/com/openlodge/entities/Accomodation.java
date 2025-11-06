package com.openlodge.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.util.List;

@Entity
@Table(name = "accomodations")
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
    @JsonManagedReference
    private List<Amenity> amenities;

    // Relación con Image (1:N)
    @OneToMany(mappedBy = "accomodation", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Image> images;

    // Relación con Review (1:N)
    @OneToMany(mappedBy = "accomodation", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Review> reviews;

    // Constructors
    public Accomodation() {}

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Double getPricePerNight() { return pricePerNight; }
    public void setPricePerNight(Double pricePerNight) { this.pricePerNight = pricePerNight; }

    public Integer getMaxGuests() { return maxGuests; }
    public void setMaxGuests(Integer maxGuests) { this.maxGuests = maxGuests; }

    public Location getLocation() { return location; }
    public void setLocation(Location location) { this.location = location; }

    public User getHost() { return host; }
    public void setHost(User host) { this.host = host; }

    public List<Amenity> getAmenities() { return amenities; }
    public void setAmenities(List<Amenity> amenities) { this.amenities = amenities; }

    public List<Image> getImages() { return images; }
    public void setImages(List<Image> images) { this.images = images; }

    public List<Review> getReviews() { return reviews; }
    public void setReviews(List<Review> reviews) { this.reviews = reviews; }

    // Builder pattern
    public static AccomodationBuilder builder() {
        return new AccomodationBuilder();
    }

    public static class AccomodationBuilder {
        private Accomodation accomodation = new Accomodation();

        public AccomodationBuilder title(String title) {
            accomodation.setTitle(title);
            return this;
        }

        public AccomodationBuilder description(String description) {
            accomodation.setDescription(description);
            return this;
        }

        public AccomodationBuilder pricePerNight(Double pricePerNight) {
            accomodation.setPricePerNight(pricePerNight);
            return this;
        }

        public AccomodationBuilder maxGuests(Integer maxGuests) {
            accomodation.setMaxGuests(maxGuests);
            return this;
        }

        public AccomodationBuilder location(Location location) {
            accomodation.setLocation(location);
            return this;
        }

        public AccomodationBuilder host(User host) {
            accomodation.setHost(host);
            return this;
        }

        public AccomodationBuilder amenities(List<Amenity> amenities) {
            accomodation.setAmenities(amenities);
            return this;
        }

        public Accomodation build() {
            return accomodation;
        }
    }
}
