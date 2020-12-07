package com.element.insurance.bookings.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.stream.Stream;

public enum BookingStatus {
    Booked("Booked"),
    Canceled("Canceled"),
    Unkown("unkown");

    private final String value;

    BookingStatus(String value) {
        this.value = value;
    }

    @JsonCreator
    public static BookingStatus fromValue(String value) {
        if (value == null) {
            return null;
        }
        return Stream.of(BookingStatus.values()).filter(e -> e.toString().equals(value)).findFirst()
                .orElse(Unkown);
    }

    @Override
    @JsonValue
    public String toString() {
        return String.valueOf(value);
    }
}
