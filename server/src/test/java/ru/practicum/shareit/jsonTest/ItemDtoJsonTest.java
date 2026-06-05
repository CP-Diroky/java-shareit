package ru.practicum.shareit.jsonTest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.item.dto.ItemDto;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JsonTest
class ItemDtoJsonTest {

    @Autowired
    private JacksonTester<ItemDto> json;

    @Test
    void shouldSerializeDates() throws Exception {

        ItemDto dto = new ItemDto();
        dto.setId(1L);
        dto.setName("PS4");

        dto.setLastBooking(
                LocalDateTime.of(2026, 6, 5, 12, 0)
        );

        dto.setNextBooking(
                LocalDateTime.of(2026, 6, 10, 12, 0)
        );

        JsonContent<ItemDto> result = json.write(dto);

        assertThat(result)
                .extractingJsonPathStringValue("$.lastBooking")
                .isEqualTo("2026-06-05T12:00:00");

        assertThat(result)
                .extractingJsonPathStringValue("$.nextBooking")
                .isEqualTo("2026-06-10T12:00:00");
    }

    @Test
    void shouldSerializeItemDto() throws Exception {
        ItemDto dto = new ItemDto();
        dto.setId(1L);
        dto.setName("PS5");
        dto.setDescription("desc");
        dto.setAvailable(true);
        dto.setComments(Collections.emptyList());

        JsonContent<ItemDto> result = json.write(dto);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo("PS5");
    }

    @Test
    void shouldDeserializeItemDto() throws Exception {
        String content = "{\"id\":1,\"name\":\"PS5\",\"description\":\"desc\"," +
                "\"available\":true,\"requestId\":10,\"ownerId\":5}";

        ItemDto dto = json.parseObject(content);

        assertThat(dto.getName()).isEqualTo("PS5");
        assertThat(dto.getAvailable()).isTrue();
        assertThat(dto.getRequestId()).isEqualTo(10L);
    }
}
