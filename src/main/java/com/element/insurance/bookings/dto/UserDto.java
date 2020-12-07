package com.element.insurance.bookings.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;
import java.util.Objects;

import static com.element.insurance.bookings.util.BookingTrailsConstants.*;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"name", "dob", "email"})
public class UserDto {

    @NotBlank(message = USER_NAME_ERROR)
    private String name;

    @Pattern(regexp = EMAIL_REGEX, message = USER_EMAIL_ERROR)
    private String email;

    @ApiModelProperty(value = "dob in (yyyy-MM-dd) format", example = "1992-06-26")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dob;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDate getDob() {
        return dob;
    }

    public void setDob(LocalDate dob) {
        this.dob = dob;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserDto)) return false;
        UserDto userDto = (UserDto) o;
        return Objects.equals(getName(), userDto.getName()) &&
                Objects.equals(getEmail(), userDto.getEmail()) &&
                Objects.equals(getDob(), userDto.getDob());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getEmail(), getDob());
    }

    @Override
    public String toString() {
        return "UserDto{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", dob=" + dob +
                '}';
    }
}
