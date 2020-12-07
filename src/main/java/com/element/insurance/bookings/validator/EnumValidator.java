package com.element.insurance.bookings.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class EnumValidator implements ConstraintValidator<ValidEnum, Enum> {
    @Override
    public boolean isValid(Enum value, ConstraintValidatorContext context) {
        if (value == null) return false;
        return !value.toString().equals("unkown");
    }
}