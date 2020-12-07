package com.element.insurance.bookings.dto;

import com.element.insurance.bookings.validator.ValidBookingDto;
import com.element.insurance.bookings.validator.ValidEnum;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@ValidBookingDto
public class BookingCreateUpdateDto {

    @ValidEnum
    @ApiModelProperty(value = "Trails value", example = "Shire, Gondor or Mordor")
    private TrailsEnum trail;

    @Valid
    private List<UserDto> hikers = new ArrayList<>();

    @Valid
    @NotNull(message = "Hiker who is booking required")
    private UserDto bookedBy;


    @ValidEnum
    @ApiModelProperty(value = "booking status", example = "Booked or Canceled")
    private BookingStatus bookingStatus = BookingStatus.Booked;

    @ApiModelProperty(value = "bookingDate in (yyyy-MM-dd) format", example = "1992-06-26")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDate bookingDate;

    @ApiModelProperty(value = "startTime in (hh:mm:ss) format", example = "21:45:00")
    @JsonFormat(pattern = "HH:mm:ss")
    @JsonDeserialize(using = LocalTimeDeserializer.class)
    @JsonSerialize(using = LocalTimeSerializer.class)
    private LocalTime startTime;

    @ApiModelProperty(value = "endTime in (hh:mm:ss) format", example = "21:45:00")
    @JsonFormat(pattern = "HH:mm:ss")
    @JsonDeserialize(using = LocalTimeDeserializer.class)
    @JsonSerialize(using = LocalTimeSerializer.class)
    private LocalTime endTime;

    public TrailsEnum getTrail() {
        return trail;
    }

    public void setTrail(TrailsEnum trail) {
        this.trail = trail;
    }

    public List<UserDto> getHikers() {
        return hikers;
    }

    public void setHikers(List<UserDto> hikers) {
        this.hikers = hikers;
    }

    public UserDto getBookedBy() {
        return bookedBy;
    }

    public void setBookedBy(UserDto bookedBy) {
        this.bookedBy = bookedBy;
    }

    public LocalDate getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(LocalDate bookingDate) {
        this.bookingDate = bookingDate;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public BookingStatus getBookingStatus() {
        return bookingStatus;
    }

    public void setBookingStatus(BookingStatus bookingStatus) {
        this.bookingStatus = bookingStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BookingCreateUpdateDto)) return false;
        BookingCreateUpdateDto that = (BookingCreateUpdateDto) o;
        return getTrail() == that.getTrail() &&
                Objects.equals(getHikers(), that.getHikers()) &&
                Objects.equals(getBookedBy(), that.getBookedBy()) &&
                getBookingStatus() == that.getBookingStatus() &&
                Objects.equals(getBookingDate(), that.getBookingDate()) &&
                Objects.equals(getStartTime(), that.getStartTime()) &&
                Objects.equals(getEndTime(), that.getEndTime());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getTrail(), getHikers(), getBookedBy(), getBookingStatus(), getBookingDate(), getStartTime(), getEndTime());
    }

    @Override
    public String toString() {
        return "BookingCreateUpdateDto{" +
                "trail=" + trail +
                ", hikers=" + hikers +
                ", bookedBy=" + bookedBy +
                ", bookingStatus=" + bookingStatus +
                ", bookingDate=" + bookingDate +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                '}';
    }
}
