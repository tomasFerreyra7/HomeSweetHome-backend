package com.openlodge.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewDTO {
    private Integer rating;
    private String comment;
    private LocalDate date;
    private Long userId;
    private Long accomodationId;
}
