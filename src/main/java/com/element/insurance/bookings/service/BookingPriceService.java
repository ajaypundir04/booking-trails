package com.element.insurance.bookings.service;

/**
 * @author Ajay Singh Pundir
 * Price calculation of the booking.
 */
@FunctionalInterface
public interface BookingPriceService {

    double calculateBookingPrice(int numberOfHikers, double trailPrice);
}
