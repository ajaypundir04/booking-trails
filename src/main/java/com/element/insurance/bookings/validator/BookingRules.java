package com.element.insurance.bookings.validator;

import com.element.insurance.bookings.dto.BookingCreateUpdateDto;
import com.element.insurance.bookings.entity.TrailEntity;

import java.util.List;

public interface BookingRules {

    List<String> validateBooking(BookingCreateUpdateDto value, TrailEntity trailEntity);
}
