package com.openlodge.dto;

import java.util.List;

import com.openlodge.entities.Location;

import lombok.Data;

@Data
public class AccomodationDTO {
    private String title;
    private String description;
    private Double pricePerNight;
    private Integer maxGuests;

    private Long hostId;
    private List<Long> amenityIds;

    private Location location;

    private List<ImageDTO> images;
}
