package com.element.insurance.bookings.validator;

import com.element.insurance.bookings.dto.BookingCreateUpdateDto;
import com.element.insurance.bookings.dto.UserDto;
import com.element.insurance.bookings.entity.TrailEntity;
import com.element.insurance.bookings.util.BookingTrailsConstants;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public enum BookingValidator implements BookingRules {

    START_END_TIME {
        @Override
        public List<String> validateBooking(BookingCreateUpdateDto value, TrailEntity trailEntity) {
            List<String> errorMessage = new ArrayList<>();
            if (value.getEndTime().isBefore(value.getStartTime()) ||
                    value.getEndTime().equals(value.getStartTime())) {
                errorMessage.add(String.format("Booking Time [%s-%s] is invalid",
                        value.getStartTime(), value.getEndTime()));

            }
            return errorMessage;
        }
    },
    AGE_LIMIT{
        @Override
        public List<String> validateBooking(BookingCreateUpdateDto value, TrailEntity trailEntity) {
            List<UserDto> hikers =BookingTrailsConstants.getHikersList(value.getBookedBy(), value.getHikers());
            List<String> errorMessage = new ArrayList<>();

            List<UserDto> invalidHikers = hikers
                    .stream().filter(u -> {
                        int age = calculateAge(u.getDob());
                        return age <= trailEntity.getLowerAge() || age >= trailEntity.getUpperAge();
                    }).collect(Collectors.toList());

            if (!invalidHikers.isEmpty()) {
                errorMessage.add(String.format("Hikers [%s] are not within age [%s-%s]limit",
                        invalidHikers, trailEntity.getLowerAge(), trailEntity.getUpperAge()));
            }
            return errorMessage;
        }
        private int calculateAge(LocalDate dateOfBirth) {
            return Period.between(dateOfBirth, LocalDate.now()).getYears();
        }
    },
    TRAIL_START_END_TIME
            {
                @Override
                public List<String> validateBooking(BookingCreateUpdateDto value, TrailEntity trailEntity) {

                    List<String> errorMessage = new ArrayList<>();
                    if (value.getEndTime().isBefore(value.getStartTime()) ||
                            value.getEndTime().equals(value.getStartTime())) {
                        errorMessage.add(String.format("Booking Time [%s-%s] is invalid",
                                value.getStartTime(), value.getEndTime()));

                    }
                    if (!(value.getStartTime().equals(trailEntity.getStartTime()) ||
                            (value.getStartTime().isAfter(trailEntity.getStartTime()) && value.getStartTime().isBefore(trailEntity.getEndTime())))
                            || !(value.getEndTime().equals(trailEntity.getEndTime()) || value.getEndTime().isBefore(trailEntity.getEndTime()))) {

                        errorMessage.add(String.format("Hiking Time [%s-%s] is not within time limit [%s-%s]limit",
                                value.getStartTime(), value.getEndTime(), trailEntity.getStartTime(), trailEntity.getEndTime()));
                    }
                    return errorMessage;
                }
            },
    UNIQUE_USER_EMAIL{
        @Override
        public List<String> validateBooking(BookingCreateUpdateDto value, TrailEntity trailEntity) {
            List<UserDto> hikers =BookingTrailsConstants.getHikersList(value.getBookedBy(), value.getHikers());
            List<String> errorMessage = new ArrayList<>();

            if (hikers.stream()
                    .map(UserDto::getEmail).collect(Collectors.toSet()).size() != hikers.size()) {
                errorMessage.add(String.format("Hikers [%s] with duplicate email",
                        value.getHikers()));
            }
            return errorMessage;
        }
    }
}
