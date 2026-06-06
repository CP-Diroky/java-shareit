package ru.practicum.shareit.request.dto;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.request.Request;

import java.util.Collection;

@UtilityClass
public class RequestMapper {

    public static RequestDto toRequestDto(Request request) {
        return new RequestDto(
                request.getId(),
                request.getDescription(),
                request.getCreated()
        );
    }

    public static RequestDto toRequestDtoWithItems(Request request, Collection<ItemDto> items) {
        return new RequestDto(
                request.getId(),
                request.getDescription(),
                request.getCreated(),
                items
        );
    }

    public static Request toRequest(RequestDto requestDto) {
        return new Request(requestDto.getDescription());
    }

}
