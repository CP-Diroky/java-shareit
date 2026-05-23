package ru.practicum.shareit.booking.dto;

import lombok.*;
import ru.practicum.shareit.booking.Booking;

import java.time.LocalDateTime;

//Dto класс для выдачи ответов

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BookingDtoResponse {

    private Long id;
    private LocalDateTime start;
    private LocalDateTime end;

    private ItemDto item;
    private BookerDto booker;

    private Booking.Status status;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ItemDto {
        private Long id;
        private String name;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class BookerDto {
        private Long id;
    }
}
