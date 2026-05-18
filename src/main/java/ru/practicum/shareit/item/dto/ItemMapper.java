package ru.practicum.shareit.item.dto;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.item.model.Item;

import java.util.Collection;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ItemMapper {

    public static ItemDto toItemDto(Item item) {
        return new ItemDto(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                item.getRequest() != null ? item.getRequest().getId() : null
        );
    }

    public static Item toItem(ItemDto itemDto) {
        return new Item(
            itemDto.getName(),
            itemDto.getDescription(),
            itemDto.getAvailable()
        );
    }

    public static Collection<ItemDto> itemDtoCollection(Collection<Item> items) {
        return items.stream().map(ItemMapper::toItemDto).toList();
    }

}
