package ru.practicum.shareit.booking;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;


@Validated
@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingClient bookingClient;


    @PostMapping
    public ResponseEntity<Object> addBooking(@RequestBody @Valid BookingDto bookingDto,
                                     @RequestHeader("X-Sharer-User-Id") @Positive Long userId) {
        return bookingClient.addBooking(userId, bookingDto);
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<Object> approveBooking(@PathVariable @Positive Long bookingId, @RequestParam boolean approved,
                                             @RequestHeader("X-Sharer-User-Id") @Positive Long userId) {
        return bookingClient.approveBooking(bookingId, approved, userId);
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<Object> getBookingById(@PathVariable @Positive Long bookingId,
                                             @RequestHeader("X-Sharer-User-Id") @Positive Long userId) {
        return bookingClient.getBookingById(bookingId, userId);
    }

    @GetMapping
    public ResponseEntity<Object> getUsersBookings(@RequestParam(defaultValue = "ALL") String state,
                                                           @RequestHeader("X-Sharer-User-Id") @Positive Long userId) {
        return bookingClient.getUsersBookings(state, userId);
    }

    @GetMapping("/owner")
    public ResponseEntity<Object> getOwnersBookings(@RequestParam(defaultValue = "ALL") String state,
                                                            @RequestHeader("X-Sharer-User-Id") @Positive Long userId) {
        return bookingClient.getOwnersBookings(state,userId);
    }

}
