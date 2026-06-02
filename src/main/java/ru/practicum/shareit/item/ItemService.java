package ru.practicum.shareit.item;

import ru.practicum.shareit.item.comment.Comment;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.Collection;

public interface ItemService {

    Item addItem(Item item, Long userId, Long requestId);

    Item updateItem(Item item, Long userId, Long itemId);

    ItemDto getItemById(Long id, Long userId);

    Collection<ItemDto> getItems(Long userId);

    Collection<Item> searchItems(String text);

    Comment addComment(Comment comment, Long itemId, Long userId);
}
