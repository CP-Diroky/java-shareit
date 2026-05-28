package ru.practicum.shareit.booking;

import jakarta.validation.constraints.Positive;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoResponse;
import ru.practicum.shareit.booking.dto.BookingMapper;

import java.util.Collection;

@Validated
@RestController
@RequestMapping(path = "/bookings")
public class BookingController {

    private final BookingService bookingService;

    @Autowired
    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }


    @PostMapping
    public BookingDtoResponse addBooking(@RequestBody BookingDto bookingDto,
                                         @RequestHeader("X-Sharer-User-Id") @Positive Long userId) {
        return BookingMapper.toBookingDto(bookingService.addBooking(bookingDto,userId));
    }

    @PatchMapping("/{bookingId}")
    public BookingDtoResponse approveBooking(@PathVariable @Positive Long bookingId, @RequestParam boolean approved,
                                     @RequestHeader("X-Sharer-User-Id") @Positive Long userId) {
        return BookingMapper.toBookingDto(bookingService.approveBooking(bookingId, approved, userId));
    }

    @GetMapping("/{bookingId}")
    public BookingDtoResponse getBookingById(@PathVariable @Positive Long bookingId,
                                     @RequestHeader("X-Sharer-User-Id") @Positive Long userId) {
        return BookingMapper.toBookingDto(bookingService.getBookingById(bookingId, userId));
    }

    @GetMapping
    public Collection<BookingDtoResponse> getUsersBookings(@RequestParam(defaultValue = "ALL") String state,
                                                   @RequestHeader("X-Sharer-User-Id") @Positive Long userId) {
        return BookingMapper.bookingDtoCollection(bookingService.getUsersBookings(state, userId));
    }

    @GetMapping("/owner")
    public Collection<BookingDtoResponse> getOwnersBookings(@RequestParam(defaultValue = "ALL") String state,
                                                   @RequestHeader("X-Sharer-User-Id") @Positive Long userId) {
        return BookingMapper.bookingDtoCollection(bookingService.getOwnersBookings(state, userId));
    }

}
