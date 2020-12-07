package com.element.insurance.bookings.validator;

import com.element.insurance.bookings.dto.BookingCreateUpdateDto;
import com.element.insurance.bookings.dto.TrailsEnum;
import com.element.insurance.bookings.dto.UserDto;
import com.element.insurance.bookings.entity.TrailEntity;
import com.element.insurance.bookings.service.TrailService;
import com.element.insurance.bookings.util.TestUtil;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

public class BookingDtoValidatorTest {

    private final ConstraintValidatorContext.ConstraintViolationBuilder builder = Mockito
            .mock(ConstraintValidatorContext.ConstraintViolationBuilder.class);
    private ConstraintValidatorContext constraintValidatorContext = Mockito
            .mock(ConstraintValidatorContext.class);

    private TrailService trailService = Mockito.mock(TrailService.class);
    private BookingDtoValidator bookingDtoValidator = new BookingDtoValidator(trailService);

    @Before
    public void setUp() {
        bookingDtoValidationSetup();
        Mockito.when(constraintValidatorContext
                .buildConstraintViolationWithTemplate(Mockito.anyString())).thenReturn(builder);
    }

    @Test
    public void testBookingDtos() {
        Assert.assertTrue(bookingDtoValidator.isValid(TestUtil.bookingCreateUpdateDto(),
                constraintValidatorContext));
        BookingCreateUpdateDto bookingCreateUpdateDto = TestUtil.bookingCreateUpdateDto();
        bookingCreateUpdateDto.setStartTime(LocalTime.of(12, 12));
        Assert.assertFalse(bookingDtoValidator.isValid(bookingCreateUpdateDto,
                constraintValidatorContext));
        bookingCreateUpdateDto = TestUtil.bookingCreateUpdateDto();
        bookingCreateUpdateDto.setEndTime(LocalTime.of(12, 12));
        Assert.assertFalse(bookingDtoValidator.isValid(bookingCreateUpdateDto,
                constraintValidatorContext));
        bookingCreateUpdateDto = TestUtil.bookingCreateUpdateDto();
        UserDto userDto = TestUtil.userDto();
        userDto.setDob(LocalDate.now());
        bookingCreateUpdateDto.getHikers().add(userDto);
        Assert.assertFalse(bookingDtoValidator.isValid(bookingCreateUpdateDto,
                constraintValidatorContext));
        bookingCreateUpdateDto = TestUtil.bookingCreateUpdateDto();
        bookingCreateUpdateDto.getHikers().add(TestUtil.userDto());
        Assert.assertFalse(bookingDtoValidator.isValid(bookingCreateUpdateDto,
                constraintValidatorContext));
    }

    private void bookingDtoValidationSetup() {
        Map<TrailsEnum, TrailEntity> trails = new HashMap<>();
        trails.put(TrailsEnum.Shire, TestUtil.trailEntity());
        Mockito.when(trailService.getTrails()).thenReturn(trails);
    }
}
