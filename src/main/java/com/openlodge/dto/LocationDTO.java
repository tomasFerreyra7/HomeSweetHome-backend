package com.openlodge.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LocationDTO {
    private Long id;
    private String country;
    private String city;
    private String address;
    private String postalCode;
    private Double latitude;
    private Double longitude;
}
