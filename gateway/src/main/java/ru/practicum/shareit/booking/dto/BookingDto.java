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
    @NotNull
    private LocalDateTime start;
    @Future
    @NotNull
    private LocalDateTime end;
    @Positive
    @NotNull
    private Long itemId;
    @Positive
    private Long bookerId;
    private BookingStatus status;
}
