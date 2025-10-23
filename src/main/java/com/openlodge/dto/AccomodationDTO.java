package com.openlodge.dto;

import java.util.List;

import lombok.Data;

@Data
public class AccomodationDTO {
    private String title;
    private String description;
    private Double pricePerNight;
    private Integer maxGuests;
    private Long locationId;
    private Long hostId;
    private List<Long> amenityIds;
}
