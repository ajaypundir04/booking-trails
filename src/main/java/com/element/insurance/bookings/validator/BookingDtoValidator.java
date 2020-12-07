package com.element.insurance.bookings.validator;

import com.element.insurance.bookings.dto.BookingCreateUpdateDto;
import com.element.insurance.bookings.entity.TrailEntity;
import com.element.insurance.bookings.service.TrailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.ArrayList;
import java.util.List;

public class BookingDtoValidator implements ConstraintValidator<ValidBookingDto, BookingCreateUpdateDto> {

    private static final Logger LOGGER = LoggerFactory.getLogger(BookingValidator.class);
    private final TrailService trailService;

    public BookingDtoValidator(TrailService trailService) {
        this.trailService = trailService;
    }

    @Override
    public boolean isValid(BookingCreateUpdateDto value, ConstraintValidatorContext context) {
        context.disableDefaultConstraintViolation();


        TrailEntity trailEntity = trailService.getTrails().get(value.getTrail());

        List<String> errorMessages = new ArrayList<>();

        for (BookingValidator validator : BookingValidator.values()) {
            errorMessages.addAll(validator.validateBooking(value, trailEntity));
        }

        LOGGER.info("Validations failures {}", errorMessages);
        errorMessages.forEach(e -> context.buildConstraintViolationWithTemplate(e).addConstraintViolation());
        return errorMessages.isEmpty();

    }


}
