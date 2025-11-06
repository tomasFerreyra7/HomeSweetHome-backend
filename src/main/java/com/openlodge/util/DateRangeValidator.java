package com.openlodge.util;

import com.openlodge.entities.Reservation;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class DateRangeValidator implements ConstraintValidator<ValidDateRange, Reservation> {
    
    @Override
    public void initialize(ValidDateRange constraintAnnotation) {
    }
    
    @Override
    public boolean isValid(Reservation reservation, ConstraintValidatorContext context) {
        if (reservation.getCheckIn() == null || reservation.getCheckOut() == null) {
            return true; // Let @NotNull handle null values
        }
        
        return reservation.getCheckOut().isAfter(reservation.getCheckIn());
    }
}
