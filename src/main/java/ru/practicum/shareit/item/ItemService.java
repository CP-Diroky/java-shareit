package ru.practicum.shareit.item;

import ru.practicum.shareit.item.model.Item;

import java.util.Collection;

public interface ItemService {

    Item addItem(Item item, Long userId);

    Item updateItem(Item item, Long userId, Long itemId);

    Item getItemById(Long id);

    Collection<Item> getItems(Long userId);

    Collection<Item> searchItems(String text);
}
