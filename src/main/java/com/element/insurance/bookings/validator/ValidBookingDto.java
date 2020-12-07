package com.element.insurance.bookings.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.annotation.Documented;

@Target({ElementType.TYPE, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {BookingDtoValidator.class})
@Documented
public @interface ValidBookingDto {

    String message() default "Invalid Booking Details";

    Class<?>[] groups() default {};

    Class<?extends Payload> [] payload() default {};

}
