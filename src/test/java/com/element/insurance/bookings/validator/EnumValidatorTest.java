package com.element.insurance.bookings.validator;

import com.element.insurance.bookings.dto.BookingStatus;
import com.element.insurance.bookings.dto.TrailsEnum;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import javax.validation.ConstraintValidatorContext;

public class EnumValidatorTest {
    private final ConstraintValidatorContext.ConstraintViolationBuilder builder = Mockito
            .mock(ConstraintValidatorContext.ConstraintViolationBuilder.class);
    private ConstraintValidatorContext constraintValidatorContext = Mockito
            .mock(ConstraintValidatorContext.class);
    private EnumValidator enumValidator = new EnumValidator();

    @Before
    public void setUp() {
        Mockito.when(constraintValidatorContext
                .buildConstraintViolationWithTemplate(Mockito.anyString())).thenReturn(builder);
    }

    @Test
    public void testEnums() {
        Assert.assertTrue(enumValidator.isValid(TrailsEnum.Shire, constraintValidatorContext));
        Assert.assertTrue(enumValidator.isValid(BookingStatus.Booked, constraintValidatorContext));
        Assert.assertFalse(enumValidator.isValid(BookingStatus.Unkown, constraintValidatorContext));
        Assert.assertFalse(enumValidator.isValid(TrailsEnum.Unkown, constraintValidatorContext));
    }
}
