package com.element.insurance.bookings.service.impl;

import com.element.insurance.bookings.dto.*;
import com.element.insurance.bookings.entity.BookingEntity;
import com.element.insurance.bookings.entity.TrailEntity;
import com.element.insurance.bookings.entity.UserEntity;
import com.element.insurance.bookings.exception.BookingException;
import com.element.insurance.bookings.repository.BookingRepository;
import com.element.insurance.bookings.repository.UserRepository;
import com.element.insurance.bookings.service.BookingPriceService;
import com.element.insurance.bookings.service.BookingTrailService;
import com.element.insurance.bookings.service.TrailService;
import com.element.insurance.bookings.util.BookingTrailsConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author Ajay Singh Pundir
 * Bookings handling service.
 */
@Service
public class BookingTrailServiceImpl implements BookingTrailService {

    private static final Logger LOGGER = LoggerFactory.getLogger(BookingTrailServiceImpl.class);

    private static final String REQUEST_NOT_EMPTY = "Request cannot be null or empty";

    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final TrailService trailService;
    private final BookingPriceService bookingPriceService;

    public BookingTrailServiceImpl(BookingRepository bookingRepository, UserRepository userRepository,
                                   TrailService trailService) {
        this.bookingRepository = bookingRepository;
        this.userRepository = userRepository;
        this.trailService = trailService;
        this.bookingPriceService = (numberOfHikers, trailPrice) -> numberOfHikers * trailPrice;
    }

    /**
     * It will create bookings
     *
     * @param bookingRequest @{@link BookingCreateUpdateDto} for creating booking.
     * @return @{@link BookingDto} as response for created entity.
     * @throws @{@link BookingException}, if already existing booking.
     */
    @Transactional
    public BookingDto create(BookingCreateUpdateDto bookingRequest) {
        Assert.notNull(bookingRequest, REQUEST_NOT_EMPTY);
        bookingRequest.setHikers(BookingTrailsConstants.getHikersList(bookingRequest.getBookedBy(), bookingRequest.getHikers()));
        LOGGER.info("Number of hikers {}", bookingRequest.getHikers().size());

        Optional<UserEntity> existingUser = userRepository.findByEmail(bookingRequest.getBookedBy().getEmail());
        if (existingUser.isPresent()) {

            TrailEntity trailEntity = trailService.getTrails().get(bookingRequest.getTrail());

            bookingRepository.findByBookedByAndTrailIdAndBookingDateAndStartTimeAndEndTime(
                    existingUser.get(), trailEntity.getId(),
                    bookingRequest.getBookingDate(), bookingRequest.getStartTime(), bookingRequest.getEndTime()
            ).ifPresent(e -> {
                throw new BookingException(String.format("Already Existing booking [%s]", e));
            });
        } else {
            userRepository.save(mapToEntity(bookingRequest.getBookedBy()));
        }


        LOGGER.info("Saving to DB {}", bookingRequest);
        BookingEntity entity = bookingRepository.save(mapToEntity(bookingRequest));
        return mapToDto(entity);
    }

    /**
     * Search Bookings by booking date.
     *
     * @param date     booking date.
     * @param pageable @{@link PageableResponse} for setting pages.
     * @return @PageableResponse<{@link BookingDto} all the bookings for a particular date.
     */
    @Override
    public PageableResponse<BookingDto> getByDate(String date, Pageable pageable) {
        PageableResponse<BookingDto> pageableResponse;
        Assert.notNull(date, "booking Date cannot be null");
        Page<BookingEntity> bookings = bookingRepository.findAllByBookingDate(LocalDate.parse(date), pageable);
        if (bookings.hasContent()) {
            List<BookingDto> bookingDtos = bookings.getContent()
                    .stream().map(this::mapToDto)
                    .collect(Collectors.toList());
            return new PageableResponse<>(bookingDtos.size(),
                    bookingDtos);

        }
        return new PageableResponse<>(0, Collections.EMPTY_LIST);
    }

    /**
     * Fetching bookings by email and bookingDate.
     *
     * @param userEmail   user who booked the hike.
     * @param bookingDate date for which hike is booked.
     * @return @{@link List<@BookingDto>} all the bookings satisfying criteria.
     */
    @Override
    public List<BookingDto> get(String userEmail, LocalDate bookingDate) {
        Assert.notNull(userEmail, "Email cannot be null");
        Assert.notNull(bookingDate, "bookingDate cannot be null");
        UserEntity userEntity = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new EntityNotFoundException(String.format("User Not Found with email [%s]", userEmail)));
        List<BookingEntity> bookingEntities = bookingRepository.findByBookedByAndBookingDate(userEntity, bookingDate)
                .orElseThrow(() -> new EntityNotFoundException("Booking Not found"));

        return bookingEntities.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    /**
     * Delete the bookings.
     *
     * @param searchDto @{@link BookingSearchDto} delete booking on the basis of criteria.
     */
    @Override
    @Transactional
    public void delete(BookingSearchDto searchDto) {
        Assert.notNull(searchDto, "search dto cannot be null");
        BookingEntity entity = getByBookedByTrailDateStartTimeEndTime(searchDto.getBookingDate(), searchDto.getTrail(),
                searchDto.getEmail(), searchDto.getStartTime(), searchDto.getEndTime());
        bookingRepository.delete(entity);
    }

    /**
     * Update booking Status.
     *
     * @param searchDto     @{@link BookingSearchDto} search booking on the basis of criteria.
     * @param bookingStatus @{@link BookingStatus} update status.
     * @return @{@link BookingDto} update entity mapped to dto.
     */
    @Override
    @Transactional
    public BookingDto updateStatus(BookingSearchDto searchDto, BookingStatus bookingStatus) {
        Assert.notNull(searchDto, "search dto cannot be null");
        Assert.notNull(bookingStatus, "bookingStatus dto cannot be null");

        BookingEntity entity = getByBookedByTrailDateStartTimeEndTime(searchDto.getBookingDate(), searchDto.getTrail(),
                searchDto.getEmail(), searchDto.getStartTime(), searchDto.getEndTime());
        entity.setBookingStatus(bookingStatus.name());
        return mapToDto(bookingRepository.save(entity));
    }

    /**
     * Update the bookings.
     *
     * @param bookingUpdateRequest @{@link BookingCreateUpdateDto} update the bookings with this dto.
     * @return @{@link BookingDto} update entity mapped to dto.
     */
    @Override
    @Transactional
    public BookingDto update(BookingCreateUpdateDto bookingUpdateRequest) {
        Assert.notNull(bookingUpdateRequest, "bookingUpdateRequest dto cannot be null");
        BookingEntity entity = getByBookedByTrailDateStartTimeEndTime(bookingUpdateRequest.getBookingDate(), bookingUpdateRequest.getTrail(),
                bookingUpdateRequest.getBookedBy().getEmail(), bookingUpdateRequest.getStartTime(), bookingUpdateRequest.getEndTime());
        bookingUpdateRequest.getHikers().remove(bookingUpdateRequest.getBookedBy());
        entity.setUsers(bookingUpdateRequest.getHikers().stream().map(u ->
                userRepository.findByEmail(u.getEmail()).orElse(mapToEntity(u))).collect(Collectors.toSet()));
        entity.setStartTime(bookingUpdateRequest.getStartTime());
        entity.setEndTime(bookingUpdateRequest.getEndTime());
        entity.setBookingDate(bookingUpdateRequest.getBookingDate());
        entity.setTrail(trailService.getTrails().get(bookingUpdateRequest.getTrail()));
        return mapToDto(bookingRepository.save(entity));
    }


    private BookingEntity getByBookedByTrailDateStartTimeEndTime(LocalDate date, TrailsEnum trail, String userEmail, LocalTime startTime, LocalTime endTime) {

        UserEntity userEntity = userRepository.findByEmail(userEmail).orElseThrow(() -> new EntityNotFoundException(String.format("User Not Found with email [%s]", userEmail)));
        return bookingRepository.findByBookedByAndTrailIdAndBookingDateAndStartTimeAndEndTime(userEntity,
                trailService.getTrails().get(trail).getId(), date,
                startTime, endTime).orElseThrow(() -> new EntityNotFoundException(String.format("Booking doesn't exists for [%s]", userEmail)));
    }

    private BookingEntity mapToEntity(BookingCreateUpdateDto bookingCreateUpdateDto) {
        BookingEntity entity = new BookingEntity();
        entity.setBookingDate(bookingCreateUpdateDto.getBookingDate());
        entity.setTrail(trailService.getTrails().get(bookingCreateUpdateDto.getTrail()));
        bookingCreateUpdateDto.getHikers().remove(bookingCreateUpdateDto.getBookedBy());

        entity.setUsers(bookingCreateUpdateDto.getHikers().stream().map(u ->
                userRepository.findByEmail(u.getEmail()).orElse(mapToEntity(u))).collect(Collectors.toSet()));

        entity.setStartTime(bookingCreateUpdateDto.getStartTime());
        entity.setEndTime(bookingCreateUpdateDto.getEndTime());
        entity.setBookingStatus(bookingCreateUpdateDto.getBookingStatus().name());
        entity.setBookedBy(userRepository.findByEmail(bookingCreateUpdateDto.getBookedBy().getEmail()).get());
        return entity;
    }

    private UserEntity mapToEntity(UserDto dto) {
        UserEntity entity = new UserEntity();
        entity.setDateOfBirth(dto.getDob());
        entity.setName(dto.getName());
        entity.setEmail(dto.getEmail());
        return entity;
    }

    private BookingDto mapToDto(BookingEntity bookingEntity) {
        BookingDto dto = new BookingDto();
        dto.setBookingStatus(BookingStatus.valueOf(bookingEntity.getBookingStatus()));
        dto.setStartTime(bookingEntity.getStartTime());
        dto.setEndTime(bookingEntity.getEndTime());
        dto.setBookingDate(bookingEntity.getBookingDate());
        List<UserDto> hikers = new ArrayList<>();
        UserDto bookedBy = mapToDto(bookingEntity.getBookedBy());
        hikers.addAll(bookingEntity.getUsers().stream().map(this::mapToDto).collect(Collectors.toList()));
        dto.setHikers(BookingTrailsConstants.getHikersList(bookedBy, hikers));
        dto.setBookingPrice(bookingPriceService.calculateBookingPrice(dto.getHikers().size(), bookingEntity.getTrail().getPrice()));
        dto.setBookedBy(mapToDto(bookingEntity.getBookedBy()));
        dto.setTrail(TrailsEnum.valueOf(bookingEntity.getTrail().getName()));
        return dto;
    }

    private UserDto mapToDto(UserEntity entity) {
        UserDto dto = new UserDto();
        dto.setDob(entity.getDateOfBirth());
        dto.setName(entity.getName());
        dto.setEmail(entity.getEmail());
        return dto;
    }

}
