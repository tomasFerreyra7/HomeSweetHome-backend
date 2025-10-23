package com.openlodge.util;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = DateRangeValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidDateRange {
    String message() default "La fecha de check-out debe ser posterior a la fecha de check-in";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
