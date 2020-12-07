package com.element.insurance.bookings.dto;

import com.element.insurance.bookings.validator.ValidEnum;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.Pattern;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;

import static com.element.insurance.bookings.util.BookingTrailsConstants.EMAIL_REGEX;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BookingSearchDto {

    @Pattern(regexp = EMAIL_REGEX, message = "Valid Email of hiker is required")
    private String email;

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

    @ValidEnum
    @ApiModelProperty(value = "trails value", example = "Shire, Gondor or Mordor")
    private TrailsEnum trail;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public TrailsEnum getTrail() {
        return trail;
    }

    public void setTrail(TrailsEnum trail) {
        this.trail = trail;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BookingSearchDto)) return false;
        BookingSearchDto that = (BookingSearchDto) o;
        return Objects.equals(getEmail(), that.getEmail()) &&
                Objects.equals(getBookingDate(), that.getBookingDate()) &&
                Objects.equals(getStartTime(), that.getStartTime()) &&
                Objects.equals(getEndTime(), that.getEndTime()) &&
                getTrail() == that.getTrail();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getEmail(), getBookingDate(), getStartTime(), getEndTime(), getTrail());
    }

    @Override
    public String toString() {
        return "BookingSearchDto{" +
                "email='" + email + '\'' +
                ", bookingDate=" + bookingDate +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", trail=" + trail +
                '}';
    }
}
