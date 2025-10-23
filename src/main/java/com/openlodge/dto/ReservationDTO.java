package com.openlodge.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReservationDTO {

    private Long id;
    private LocalDate checkIn;
    private LocalDate checkOut;
    private Double totalPrice;
    private Long guestId;
    private Long accomodationId;
    private Long paymentId;
}
