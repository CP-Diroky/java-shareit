package ru.practicum.shareit.item;


import ru.practicum.shareit.item.model.Item;

import java.util.Collection;

public interface ItemRepository {

    Item saveItem(Item Item);

    Collection<Item> getItems();

    Item getItemById(Long id);

    Item updateItem(Item Item, Long itemId);

    void deleteItem(Long id);
}
