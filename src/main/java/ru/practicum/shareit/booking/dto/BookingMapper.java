package ru.practicum.shareit.booking.dto;

import ru.practicum.shareit.booking.Booking;

import java.util.Collection;

public class BookingMapper {

    public static BookingDtoResponse toBookingDto(Booking booking) {

        return new BookingDtoResponse(
                booking.getId(),
                booking.getStart(),
                booking.getEnd(),

                new BookingDtoResponse.ItemDto(
                        booking.getItem().getId(),
                        booking.getItem().getName()
                ),

                new BookingDtoResponse.BookerDto(
                        booking.getBooker().getId()
                ),

                booking.getStatus()
        );
    }

    public static Collection<BookingDtoResponse> bookingDtoCollection(Collection<Booking> bookings) {
        return bookings.stream()
                .map(BookingMapper::toBookingDto)
                .toList();
    }

}



