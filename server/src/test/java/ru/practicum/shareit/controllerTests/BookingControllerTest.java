package ru.practicum.shareit.controllerTests;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingController;
import ru.practicum.shareit.booking.BookingService;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.user.User;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookingController.class)
class BookingControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    BookingService bookingService;

    @Test
    void shouldAddBooking() throws Exception {

        Item item = new Item();
        item.setId(1L);
        item.setName("Drill");

        User user = new User();
        user.setId(1L);

        Booking booking = new Booking();
        booking.setId(1L);
        booking.setStatus(Booking.Status.WAITING);
        booking.setItem(item);
        booking.setBooker(user);

        when(bookingService.addBooking(any(), eq(1L))).thenReturn(booking);

        mockMvc.perform(post("/bookings")
                        .header("X-Sharer-User-Id", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"start\":\"2026-06-10T10:00:00\",\"end\":\"2026-06-12T10:00:00\",\"itemId\":1}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("WAITING"));
    }

    @Test
    void shouldApproveBooking() throws Exception {

        Item item = new Item();
        item.setId(1L);
        item.setName("Drill");

        User user = new User();
        user.setId(1L);

        Booking booking = new Booking();
        booking.setId(1L);
        booking.setStatus(Booking.Status.APPROVED);
        booking.setItem(item);
        booking.setBooker(user);

        when(bookingService.approveBooking(1L, true, 1L)).thenReturn(booking);

        mockMvc.perform(patch("/bookings/1")
                        .header("X-Sharer-User-Id", 1L)
                        .param("approved", "true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("APPROVED"));
    }

    @Test
    void shouldGetBookingById() throws Exception {

        Item item = new Item();
        item.setId(1L);
        item.setName("Drill");

        User user = new User();
        user.setId(1L);

        Booking booking = new Booking();
        booking.setId(1L);
        booking.setStatus(Booking.Status.WAITING);
        booking.setItem(item);
        booking.setBooker(user);

        when(bookingService.getBookingById(1L, 1L))
                .thenReturn(booking);

        mockMvc.perform(get("/bookings/1")
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void shouldGetBookings() throws Exception {

        when(bookingService.getUsersBookings("ALL", 1L))
                .thenReturn(List.of());

        mockMvc.perform(get("/bookings")
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk());
    }

    @Test
    void shouldGetOwnerBookingsAll() throws Exception {

        when(bookingService.getOwnersBookings("ALL", 1L))
                .thenReturn(List.of());

        mockMvc.perform(get("/bookings/owner")
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk());
    }

    @Test
    void shouldGetCurrentBookings() throws Exception {

        when(bookingService.getUsersBookings("CURRENT", 1L))
                .thenReturn(List.of());

        mockMvc.perform(get("/bookings")
                        .header("X-Sharer-User-Id", 1L)
                        .param("state", "CURRENT"))
                .andExpect(status().isOk());
    }

    @Test
    void shouldGetPastBookings() throws Exception {

        when(bookingService.getUsersBookings("PAST", 1L))
                .thenReturn(List.of());

        mockMvc.perform(get("/bookings")
                        .header("X-Sharer-User-Id", 1L)
                        .param("state", "PAST"))
                .andExpect(status().isOk());
    }

}
