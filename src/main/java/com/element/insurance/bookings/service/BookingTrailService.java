package com.element.insurance.bookings.service;

import com.element.insurance.bookings.dto.BookingDto;
import com.element.insurance.bookings.dto.BookingCreateUpdateDto;
import com.element.insurance.bookings.dto.BookingSearchDto;
import com.element.insurance.bookings.dto.BookingStatus;
import com.element.insurance.bookings.dto.PageableResponse;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

/**
 * @author Ajay Singh Pundir
 * Booking Trail service
 */
public interface BookingTrailService {
    BookingDto create(BookingCreateUpdateDto bookingRequest);

    PageableResponse<BookingDto> getByDate(String date, Pageable pageable);

    List<BookingDto> get(String userEmail, LocalDate bookingDate);

    void delete(BookingSearchDto searchDto);

    BookingDto updateStatus(BookingSearchDto searchDto, BookingStatus bookingStatus);

    BookingDto update(BookingCreateUpdateDto bookingUpdateRequest);
}
