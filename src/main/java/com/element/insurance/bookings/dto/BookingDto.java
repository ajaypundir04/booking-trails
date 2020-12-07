package com.element.insurance.bookings.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"trail", "bookedBy", "hikers", "date", "startTime", "endTime", "bookingPrice"})
public class BookingDto extends BookingCreateUpdateDto {


    private Double bookingPrice;

    public Double getBookingPrice() {
        return bookingPrice;
    }

    public void setBookingPrice(Double bookingPrice) {
        this.bookingPrice = bookingPrice;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BookingDto)) return false;
        if (!super.equals(o)) return false;
        BookingDto that = (BookingDto) o;
        return Objects.equals(getBookingPrice(), that.getBookingPrice());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getBookingPrice());
    }

    @Override
    public String toString() {
        return "BookingDto{" +
                "bookingPrice=" + bookingPrice +
                 BookingCreateUpdateDto.class.getSimpleName() + super.toString() +
                '}';
    }
}
