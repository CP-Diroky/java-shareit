package ru.practicum.shareit.item;


import ru.practicum.shareit.item.model.Item;

import java.util.Collection;

public interface ItemRepository {

    Item saveItem(Item item);

    Collection<Item> getItems();

    Item getItemById(Long id);

    Item updateItem(Item item, Long itemId);

    void deleteItem(Long id);
}
