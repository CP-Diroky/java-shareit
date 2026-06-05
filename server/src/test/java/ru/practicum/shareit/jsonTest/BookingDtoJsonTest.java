package ru.practicum.shareit.jsonTest;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.dto.BookingDto;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JsonTest
class BookingDtoJsonTest {

    @Autowired
    private JacksonTester<BookingDto> json;

    @Test
    void shouldSerializeBookingDto() throws Exception {

        BookingDto bookingDto = new BookingDto(
                1L,
                LocalDateTime.of(2026, 6, 10, 10, 0),
                LocalDateTime.of(2026, 6, 12, 10, 0),
                2L,
                3L,
                Booking.Status.WAITING
        );

        JsonContent<BookingDto> result = json.write(bookingDto);

        assertThat(result).extractingJsonPathNumberValue("$.id")
                .isEqualTo(1);

        assertThat(result).extractingJsonPathStringValue("$.start")
                .isEqualTo("2026-06-10T10:00:00");

        assertThat(result).extractingJsonPathStringValue("$.end")
                .isEqualTo("2026-06-12T10:00:00");

        assertThat(result).extractingJsonPathNumberValue("$.itemId")
                .isEqualTo(2);

        assertThat(result).extractingJsonPathStringValue("$.status")
                .isEqualTo("WAITING");
    }

    @Test
    void shouldDeserializeBookingDto() throws Exception {

        String content = "{\"id\":1,\"start\":\"2026-06-10T10:00:00\"," +
                "\"end\":\"2026-06-12T10:00:00\",\"itemId\":2,\"bookerId\":3,\"status\":\"WAITING\"}";

        BookingDto bookingDto = json.parseObject(content);

        Assertions.assertEquals(1L, bookingDto.getId());
        Assertions.assertEquals(2L, bookingDto.getItemId());
        Assertions.assertEquals(3L, bookingDto.getBookerId());
        Assertions.assertEquals(Booking.Status.WAITING, bookingDto.getStatus());

        Assertions.assertEquals(LocalDateTime.of(2026, 6, 10, 10, 0), bookingDto.getStart());
    }
}
