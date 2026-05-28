package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingDto;

import java.util.Collection;

public interface BookingService {

    Booking addBooking(BookingDto bookingDto, Long userId);

    Booking approveBooking(Long bookingId, boolean approved, Long userId);

    Booking getBookingById(Long bookingId, Long userId);

    Collection<Booking> getUsersBookings(String state, Long userId);

    Collection<Booking> getOwnersBookings(String state, Long userId);
}
