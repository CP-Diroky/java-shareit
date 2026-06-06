package ru.practicum.shareit.jsonTest;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.request.dto.RequestDto;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;


@JsonTest
class RequestDtoJsonTest {

    @Autowired
    private JacksonTester<RequestDto> json;

    @Test
    void shouldSerializeRequestDto() throws Exception {

        RequestDto dto = new RequestDto(
                1L,
                "Need PS5",
                LocalDateTime.of(2026, 6, 5, 12, 30),
                List.of()
        );

        JsonContent<RequestDto> result = json.write(dto);

        assertThat(result).extractingJsonPathNumberValue("$.id")
                .isEqualTo(1);

        assertThat(result).extractingJsonPathStringValue("$.description")
                .isEqualTo("Need PS5");

        assertThat(result).extractingJsonPathStringValue("$.created")
                .isEqualTo("2026-06-05T12:30:00");
    }

    @Test
    void shouldDeserializeRequestDto() throws Exception {

        String content = "{\"id\":1,\"description\":\"Need PS5\",\"created\":\"2026-06-05T12:30:00\",\"items\":[]}";

        RequestDto dto = json.parseObject(content);

        Assertions.assertEquals(1L, dto.getId());
        Assertions.assertEquals("Need PS5", dto.getDescription());

        Assertions.assertEquals(
                LocalDateTime.of(2026, 6, 5, 12, 30),
                dto.getCreated()
        );

        Assertions.assertTrue(dto.getItems().isEmpty());
    }
}

