package com.element.insurance.bookings.util;

import com.element.insurance.bookings.dto.*;
import com.element.insurance.bookings.entity.BookingEntity;
import com.element.insurance.bookings.entity.TrailEntity;
import com.element.insurance.bookings.entity.UserEntity;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class TestUtil {

    public static BookingDto bookingDto() {
        BookingDto dto = new BookingDto();
        dto.setBookingPrice(29.90);
        List<UserDto> hikers = new ArrayList<>();
        hikers.add(userDto());
        dto.setHikers(hikers);
        dto.setBookingDate(LocalDate.parse("1992-10-25"));
        dto.setTrail(TrailsEnum.Shire);
        dto.setStartTime(LocalTime.of(7, 00, 00));
        dto.setEndTime(LocalTime.of(9, 00, 00));
        dto.setBookedBy(userDto());
        dto.setBookingStatus(BookingStatus.Booked);
        return dto;
    }

    public static BookingCreateUpdateDto bookingCreateUpdateDto() {
        BookingCreateUpdateDto dto = new BookingCreateUpdateDto();
        List<UserDto> hikers = new ArrayList<>();
        hikers.add(userDto());
        dto.setHikers(hikers);
        dto.setBookingDate(LocalDate.parse("1992-10-25"));
        dto.setTrail(TrailsEnum.Shire);
        dto.setStartTime(LocalTime.of(7, 00, 00));
        dto.setEndTime(LocalTime.of(9, 00, 00));
        dto.setBookedBy(userDto());
        dto.setBookingStatus(BookingStatus.Booked);
        return dto;
    }

    public static UserDto userDto() {
        UserDto userDto = new UserDto();
        userDto.setDob(LocalDate.parse("1992-06-26"));
        userDto.setEmail("ajay@gmail.com");
        userDto.setName("ajay");
        return userDto;
    }

    public static TrailEntity trailEntity() {
        TrailEntity entity = new TrailEntity();
        entity.setId(1L);
        entity.setStartTime(LocalTime.of(7, 00, 00));
        entity.setEndTime(LocalTime.of(9, 00, 00));
        entity.setPrice(29.90);
        entity.setLowerAge(5);
        entity.setUpperAge(100);
        entity.setName(TrailsEnum.Shire.name());
        return entity;
    }

    public static BookingSearchDto bookingSearchDto() {
        BookingSearchDto dto = new BookingSearchDto();
        dto.setTrail(TrailsEnum.Shire);
        dto.setStartTime(LocalTime.of(7, 00, 00));
        dto.setEndTime(LocalTime.of(9, 00, 00));
        dto.setEmail("ajay@gmail.com");
        dto.setBookingDate(LocalDate.parse("1992-10-25"));
        return dto;
    }

    public static UserEntity userEntity() {
        UserEntity entity = new UserEntity();
        entity.setId(1L);
        entity.setEmail("ajay@gmail.com");
        entity.setDateOfBirth(LocalDate.parse("1992-06-26"));
        entity.setName("ajay");
        return entity;
    }

    public static BookingEntity bookingEntity()
    {
        BookingEntity entity = new BookingEntity();
        entity.setId(1L);
        entity.setBookingStatus(BookingStatus.Booked.name());
        Set<UserEntity> users = new HashSet<>();
        users.add(userEntity());
        entity.setUsers(users);
        entity.setStartTime(LocalTime.of(7, 00, 00));
        entity.setEndTime(LocalTime.of(9, 00, 00));
        entity.setTrail(trailEntity());
        entity.setBookingDate(LocalDate.parse("1992-10-25"));
        entity.setBookedBy(TestUtil.userEntity());
        return entity;
    }

}
