package com.element.insurance.bookings.controller;

import com.element.insurance.bookings.dto.*;
import com.element.insurance.bookings.service.BookingTrailService;
import com.element.insurance.bookings.validator.ValidBookingDto;
import com.element.insurance.bookings.validator.ValidEnum;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;
import java.util.List;

import static com.element.insurance.bookings.util.BookingTrailsConstants.*;

/**
 * @author Ajay Singh Pundir
 * REST endpoints for performing booking related operations.
 */
@Api(value = "Booking Trails Controller")
@RestController
@Validated
@RequestMapping("/v1")
public class BookingController {

    private static final Logger LOGGER = LoggerFactory.getLogger(BookingController.class);

    private final BookingTrailService bookingTrailService;

    public BookingController(BookingTrailService bookingTrailService) {
        this.bookingTrailService = bookingTrailService;
    }

    @ApiOperation(value = "Create Booking")
    @PostMapping("/bookings/")
    @ResponseStatus(value = HttpStatus.CREATED)
    public BookingDto create(@ValidBookingDto @RequestBody BookingCreateUpdateDto bookingRequest) {
        try {
            LOGGER.info("Starting- Create Booking [{}]", bookingRequest);
            return bookingTrailService.create(bookingRequest);
        } finally {
            LOGGER.info("Finished processing POST Booking type request.");
        }
    }

    @ApiOperation(value = "Get All Bookings")
    @GetMapping("/bookings/{bookingDate}")
    @ResponseStatus(value = HttpStatus.OK)
    public PageableResponse<BookingDto> getByDate(@Pattern(regexp = DATE_REGEX, message = DATE_ERROR)
                                                  @PathVariable String bookingDate, @PageableDefault(size = 2000) Pageable pageable) {
        try {
            LOGGER.info("Starting- Fetching Bookings for date {}", bookingDate);
            return bookingTrailService.getByDate(bookingDate, pageable);
        } finally {
            LOGGER.info("Finished processing GET Bookings.");
        }
    }

    @ApiOperation(value = "Search Bookings")
    @GetMapping("/bookings/")
    @ResponseStatus(value = HttpStatus.OK)
    public List<BookingDto> searchBooking(@RequestParam(value = "bookingDate") @Pattern(regexp = DATE_REGEX, message = DATE_ERROR) String bookingDate,
                                          @RequestParam(value = "userEmail") @Pattern(regexp = EMAIL_REGEX, message = USER_EMAIL_ERROR) String userEmail) {
        try {

            LOGGER.info("Starting- Fetching Bookings for email {} and bookingDate {}", userEmail, bookingDate);
            return bookingTrailService.get(userEmail, LocalDate.parse(bookingDate));
        } finally {
            LOGGER.info("Finished processing GET Bookings.");
        }
    }

    @ApiOperation(value = "Delete/Cancel Bookings")
    @DeleteMapping("/bookings/")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void delete(
            @Valid @RequestBody BookingSearchDto searchDto
    ) {
        try {
            LOGGER.info("Starting- Deleting Bookings {}", searchDto.toString());
            bookingTrailService.delete(searchDto);
        } finally {
            LOGGER.info("Finished processing DELETE Bookings.");
        }
    }

    @ApiOperation(value = "Update Booking Status")
    @PutMapping("/bookings/{bookingStatus}")
    @ResponseStatus(value = HttpStatus.OK)
    public BookingDto updateStatus(
            @Valid @RequestBody BookingSearchDto searchDto, @PathVariable @ValidEnum BookingStatus bookingStatus
    ) {
        try {
            LOGGER.info("Starting- Updating booking status {}", searchDto.toString());
            return bookingTrailService.updateStatus(searchDto, bookingStatus);
        } finally {
            LOGGER.info("Finished processing PUT Bookings.");
        }
    }

    @ApiOperation(value = "Update Bookings")
    @PutMapping("/bookings/")
    @ResponseStatus(value = HttpStatus.OK)
    public BookingDto update(
            @Valid @RequestBody BookingCreateUpdateDto bookingUpdateRequest

    ) {
        try {
            LOGGER.info("Starting- Updating Bookings {}", bookingUpdateRequest.toString());
            return bookingTrailService.update(bookingUpdateRequest);
        } finally {
            LOGGER.info("Finished processing PUT Bookings.");
        }
    }
}
