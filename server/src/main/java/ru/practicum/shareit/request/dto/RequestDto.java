package ru.practicum.shareit.request.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.shareit.item.dto.ItemDto;

import java.time.LocalDateTime;
import java.util.Collection;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RequestDto {
    private Long id;
    private String description;
    private LocalDateTime created;
    private Collection<ItemDto> items;

    public RequestDto(Long id, String description, LocalDateTime created) {
        this.id = id;
        this.description = description;
        this.created = created;
    }
}
