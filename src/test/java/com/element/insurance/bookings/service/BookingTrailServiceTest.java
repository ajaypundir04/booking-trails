package com.element.insurance.bookings.service;

import com.element.insurance.bookings.dto.*;
import com.element.insurance.bookings.entity.BookingEntity;
import com.element.insurance.bookings.entity.TrailEntity;
import com.element.insurance.bookings.entity.UserEntity;
import com.element.insurance.bookings.exception.BookingException;
import com.element.insurance.bookings.repository.BookingRepository;
import com.element.insurance.bookings.repository.UserRepository;
import com.element.insurance.bookings.service.impl.BookingTrailServiceImpl;
import com.element.insurance.bookings.util.TestUtil;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

public class BookingTrailServiceTest {
    private BookingRepository bookingRepository = Mockito.mock(BookingRepository.class);
    private UserRepository userRepository = Mockito.mock(UserRepository.class);
    private TrailService trailService = Mockito.mock(TrailService.class);
    private BookingPriceService bookingPriceService = Mockito.mock(BookingPriceService.class);
    private BookingTrailService bookingTrailService = new BookingTrailServiceImpl(bookingRepository,
            userRepository, trailService);

    @Before
    public void setUp() {
        Mockito.reset(bookingRepository, userRepository, trailService, bookingPriceService);
    }


    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithNullRequest() {
        bookingTrailService.create(null);
    }

    @Test
    public void testCreate() {
        trailsMapSetup();
        Mockito.when(userRepository.findByEmail(Mockito.anyString())).thenReturn(Optional.empty()).thenReturn(Optional.of(TestUtil.userEntity()));
        Mockito.when(bookingRepository.findByBookedByAndTrailIdAndBookingDateAndStartTimeAndEndTime(Mockito.any(UserEntity.class),
                Mockito.anyLong(), Mockito.any(LocalDate.class), Mockito.any(LocalTime.class), Mockito.any(LocalTime.class)))
                .thenReturn(Optional.empty());
        Mockito.when(bookingRepository.save(Mockito.any(BookingEntity.class))).thenReturn(TestUtil.bookingEntity());
        Mockito.when(bookingPriceService.calculateBookingPrice(Mockito.anyInt(), Mockito.anyDouble())).thenReturn(29.9);
        Assert.assertThat(bookingTrailService.create(TestUtil.bookingCreateUpdateDto()),
                Matchers.equalTo(TestUtil.bookingDto()));
    }

    @Test(expected = BookingException.class)
    public void testCreateAlreadyExisting() {
        trailsMapSetup();
        Mockito.when(userRepository.findByEmail(Mockito.anyString())).thenReturn(Optional.of(TestUtil.userEntity()));
        Mockito.when(bookingRepository.findByBookedByAndTrailIdAndBookingDateAndStartTimeAndEndTime(Mockito.any(UserEntity.class),
                Mockito.anyLong(), Mockito.any(LocalDate.class), Mockito.any(LocalTime.class), Mockito.any(LocalTime.class)))
                .thenReturn(Optional.of(TestUtil.bookingEntity()));
        bookingTrailService.create(TestUtil.bookingCreateUpdateDto());
        Mockito.verify(bookingRepository, Mockito.times(0)).save(Mockito.any(BookingEntity.class));
        Mockito.verify(bookingPriceService, Mockito.times(0)).calculateBookingPrice(Mockito.anyInt(), Mockito.anyDouble());
    }

    @Test
    public void testGetByDate() {
        List<BookingEntity> entities = new ArrayList<>();
        List<BookingDto> dtos = new ArrayList<>();
        entities.add(TestUtil.bookingEntity());
        dtos.add(TestUtil.bookingDto());
        Mockito.when(bookingRepository.findAllByBookingDate(Mockito.any(LocalDate.class), Mockito.any(Pageable.class)))
                .thenReturn(new PageImpl(entities));

        Assert.assertThat(bookingTrailService.getByDate("1992-10-25", Pageable.unpaged()),
                Matchers.equalTo(new PageableResponse<>(dtos.size(),
                        dtos)));
    }

    @Test
    public void testGetByDateNoDataFound() {
        List<BookingEntity> entities = new ArrayList<>();
        List<BookingDto> dtos = new ArrayList<>();
        Mockito.when(bookingRepository.findAllByBookingDate(Mockito.any(LocalDate.class), Mockito.any(Pageable.class)))
                .thenReturn(new PageImpl(entities));
        Assert.assertThat(bookingTrailService.getByDate("1992-10-25", Pageable.unpaged()),
                Matchers.equalTo(new PageableResponse<>(dtos.size(),
                        dtos)));
    }

    @Test
    public void testGet() {
        List<BookingEntity> entities = new ArrayList<>();
        List<BookingDto> dtos = new ArrayList<>();
        entities.add(TestUtil.bookingEntity());
        dtos.add(TestUtil.bookingDto());
        Mockito.when(bookingRepository.findByBookedByAndBookingDate(Mockito.any(UserEntity.class), Mockito.any(LocalDate.class)))
                .thenReturn(Optional.of(entities));
        Mockito.when(userRepository.findByEmail(Mockito.anyString())).thenReturn(Optional.of(TestUtil.userEntity()));
        Assert.assertThat(bookingTrailService.get("ajay@gmail.com", LocalDate.parse("1992-10-25")),
                Matchers.equalTo(dtos));
    }

    @Test(expected = EntityNotFoundException.class)
    public void testGetUserNotFound() {
        Mockito.when(userRepository.findByEmail(Mockito.anyString())).thenReturn(Optional.empty());
        bookingTrailService.get("ajay@gmail.com", LocalDate.parse("1992-10-25"));
        Mockito.verify(bookingRepository, Mockito.times(0))
                .findByBookedByAndBookingDate(Mockito.any(UserEntity.class), Mockito.any(LocalDate.class));

    }

    @Test(expected = EntityNotFoundException.class)
    public void testGetBookingNotFound() {

        Mockito.when(bookingRepository.findByBookedByAndBookingDate(Mockito.any(UserEntity.class), Mockito.any(LocalDate.class)))
                .thenReturn(Optional.empty());
        Mockito.when(userRepository.findByEmail(Mockito.anyString())).thenReturn(Optional.of(TestUtil.userEntity()));
        bookingTrailService.get("ajay@gmail.com", LocalDate.parse("1992-10-25"));
    }

    @Test
    public void testDelete() {
        trailsMapSetup();
        BookingSearchDto searchDto = TestUtil.bookingSearchDto();
        Mockito.when(bookingRepository.findByBookedByAndTrailIdAndBookingDateAndStartTimeAndEndTime(Mockito.any(UserEntity.class),
                Mockito.anyLong(), Mockito.any(LocalDate.class), Mockito.any(LocalTime.class), Mockito.any(LocalTime.class)))
                .thenReturn(Optional.of(TestUtil.bookingEntity()));
        Mockito.when(userRepository.findByEmail(Mockito.anyString())).thenReturn(Optional.of(TestUtil.userEntity()));
        Mockito.doNothing().when(bookingRepository).delete(Mockito.any(BookingEntity.class));
        bookingTrailService.delete(searchDto);
    }

    @Test(expected = EntityNotFoundException.class)
    public void testDeleteUserNotFound() {
        trailsMapSetup();
        BookingSearchDto searchDto = TestUtil.bookingSearchDto();
        Mockito.when(userRepository.findByEmail(Mockito.anyString())).thenReturn(Optional.empty());
        bookingTrailService.delete(searchDto);
        Mockito.verify(bookingRepository, Mockito.times(0))
                .findByBookedByAndTrailIdAndBookingDateAndStartTimeAndEndTime(Mockito.any(UserEntity.class),
                        Mockito.anyLong(), Mockito.any(LocalDate.class), Mockito.any(LocalTime.class), Mockito.any(LocalTime.class));
        Mockito.verify(bookingRepository, Mockito.times(0))
                .delete(Mockito.any(BookingEntity.class));
    }

    @Test(expected = EntityNotFoundException.class)
    public void testDeleteBookingNotFound() {
        trailsMapSetup();
        BookingSearchDto searchDto = TestUtil.bookingSearchDto();
        Mockito.when(bookingRepository.findByBookedByAndTrailIdAndBookingDateAndStartTimeAndEndTime(Mockito.any(UserEntity.class),
                Mockito.anyLong(), Mockito.any(LocalDate.class), Mockito.any(LocalTime.class), Mockito.any(LocalTime.class)))
                .thenReturn(Optional.empty());
        Mockito.when(userRepository.findByEmail(Mockito.anyString())).thenReturn(Optional.of(TestUtil.userEntity()));
        bookingTrailService.delete(searchDto);
        Mockito.verify(bookingRepository, Mockito.times(0))
                .delete(Mockito.any(BookingEntity.class));
    }

    @Test
    public void testUpdateStatus() {
        trailsMapSetup();
        BookingSearchDto searchDto = TestUtil.bookingSearchDto();
        BookingEntity bookingEntity = TestUtil.bookingEntity();
        Mockito.when(bookingRepository.findByBookedByAndTrailIdAndBookingDateAndStartTimeAndEndTime(Mockito.any(UserEntity.class),
                Mockito.anyLong(), Mockito.any(LocalDate.class), Mockito.any(LocalTime.class), Mockito.any(LocalTime.class)))
                .thenReturn(Optional.of(bookingEntity));
        Mockito.when(userRepository.findByEmail(Mockito.anyString())).thenReturn(Optional.of(TestUtil.userEntity()));
        BookingDto expected = TestUtil.bookingDto();
        expected.setBookingStatus(BookingStatus.Canceled);
        bookingEntity.setBookingStatus(BookingStatus.Canceled.name());
        Mockito.when(bookingRepository.save(Mockito.any(BookingEntity.class))).thenReturn(bookingEntity);
        Assert.assertThat(bookingTrailService.updateStatus(searchDto, BookingStatus.Canceled),
                Matchers.equalTo(expected));

    }


    @Test(expected = EntityNotFoundException.class)
    public void testUpdateStatusBookingNotFound() {
        trailsMapSetup();
        BookingSearchDto searchDto = TestUtil.bookingSearchDto();
        Mockito.when(bookingRepository.findByBookedByAndTrailIdAndBookingDateAndStartTimeAndEndTime(Mockito.any(UserEntity.class),
                Mockito.anyLong(), Mockito.any(LocalDate.class), Mockito.any(LocalTime.class), Mockito.any(LocalTime.class)))
                .thenReturn(Optional.empty());
        Mockito.when(userRepository.findByEmail(Mockito.anyString())).thenReturn(Optional.of(TestUtil.userEntity()));
        bookingTrailService.updateStatus(searchDto, BookingStatus.Canceled);
        Mockito.verify(bookingRepository, Mockito.times(0)).save(Mockito.any(BookingEntity.class));

    }

    @Test
    public void testUpdate() {
        trailsMapSetup();
        BookingCreateUpdateDto updateDto = TestUtil.bookingCreateUpdateDto();
        updateDto.setBookingStatus(BookingStatus.Canceled);
        BookingEntity bookingEntity = TestUtil.bookingEntity();
        Mockito.when(bookingRepository.findByBookedByAndTrailIdAndBookingDateAndStartTimeAndEndTime(Mockito.any(UserEntity.class),
                Mockito.anyLong(), Mockito.any(LocalDate.class), Mockito.any(LocalTime.class), Mockito.any(LocalTime.class)))
                .thenReturn(Optional.of(bookingEntity));
        Mockito.when(userRepository.findByEmail(Mockito.anyString())).thenReturn(Optional.of(TestUtil.userEntity()));
        BookingDto expected = TestUtil.bookingDto();
        expected.setBookingStatus(BookingStatus.Canceled);
        bookingEntity.setBookingStatus(BookingStatus.Canceled.name());
        Mockito.when(bookingRepository.save(Mockito.any(BookingEntity.class))).thenReturn(bookingEntity);
        Assert.assertThat(bookingTrailService.update(updateDto),
                Matchers.equalTo(expected));

    }


    @Test(expected = EntityNotFoundException.class)
    public void testUpdateBookingNotFound() {
        trailsMapSetup();
        BookingCreateUpdateDto updateDto = TestUtil.bookingCreateUpdateDto();
        Mockito.when(bookingRepository.findByBookedByAndTrailIdAndBookingDateAndStartTimeAndEndTime(Mockito.any(UserEntity.class),
                Mockito.anyLong(), Mockito.any(LocalDate.class), Mockito.any(LocalTime.class), Mockito.any(LocalTime.class)))
                .thenReturn(Optional.empty());
        Mockito.when(userRepository.findByEmail(Mockito.anyString())).thenReturn(Optional.of(TestUtil.userEntity()));
        bookingTrailService.update(updateDto);
        Mockito.verify(bookingRepository, Mockito.times(0)).save(Mockito.any(BookingEntity.class));

    }

    private void trailsMapSetup() {
        Map<TrailsEnum, TrailEntity> trails = new HashMap<>();
        trails.put(TrailsEnum.Shire, TestUtil.trailEntity());
        Mockito.when(trailService.getTrails()).thenReturn(trails);
    }
}
