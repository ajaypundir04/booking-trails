package com.element.insurance.bookings.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.stream.Stream;

public enum TrailsEnum {

    Shire("Shire"),
    Gondor("Gondor"),
    Mordor("Mordor"),
    Unkown("unkown");

    private final String value;

    TrailsEnum(String value) {
        this.value = value;
    }

    @JsonCreator
    public static TrailsEnum fromValue(String value) {
        if (value == null) {
            return null;
        }
        return Stream.of(TrailsEnum.values()).filter(e -> e.toString().equals(value)).findFirst()
                .orElse(Unkown);
    }

    @Override
    @JsonValue
    public String toString() {

        return String.valueOf(value);
    }
}
