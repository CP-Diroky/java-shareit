package ru.practicum.shareit.booking.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookingDto {
    @FutureOrPresent
    private LocalDateTime start;
    @Future
    private LocalDateTime end;
    @Positive
    private Long itemId;
    @Positive
    private Long bookerId;
    private BookingStatus status;
}
