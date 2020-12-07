package com.element.insurance.bookings.util;

import com.element.insurance.bookings.dto.UserDto;

import java.util.List;

public class BookingTrailsConstants {

    public final static String USER_NAME_ERROR = "user name cannot be empty or null";
    public final static String USER_EMAIL_ERROR = "user email is invalid";
    public final static String EMAIL_REGEX = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";
    public final static String DATE_REGEX = "^\\d{4}-\\d{2}-\\d{2}$";
    public final static String DATE_ERROR = "Invalid Date";

    private BookingTrailsConstants() {
    }

    public static List<UserDto> getHikersList(UserDto bookedBy, List<UserDto> hikers) {
        if (hikers.isEmpty() || !hikers.contains(bookedBy)) {
            hikers.add(bookedBy);
        }
        return hikers;
    }
}
