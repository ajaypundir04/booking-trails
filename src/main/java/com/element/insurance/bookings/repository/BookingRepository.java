package com.element.insurance.bookings.repository;

import com.element.insurance.bookings.entity.BookingEntity;
import com.element.insurance.bookings.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<BookingEntity, Long> {

    Page<BookingEntity> findAllByBookingDate(LocalDate bookingDate, Pageable pageable);


    Optional<BookingEntity> findByBookedByAndTrailIdAndBookingDateAndStartTimeAndEndTime(
            UserEntity userEntity, Long trailId, LocalDate bookingDate, LocalTime startTime,
            LocalTime endTime
    );

    Optional<List<BookingEntity>> findByBookedByAndBookingDate(
            UserEntity userEntity, LocalDate bookingDate
    );

}
