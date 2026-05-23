package ru.practicum.shareit.booking.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.booking.Booking;

import java.time.LocalDateTime;

//Dto класс для получения запросов

@Getter
@Setter
@AllArgsConstructor
public class BookingDto {

    private Long id;
    private LocalDateTime start;
    private LocalDateTime end;
    private Long itemId;
    private Long bookerId;
    private Booking.Status status;

}
