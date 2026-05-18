package ru.practicum.shareit.item;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Repository
public class ItemRepositoryImpl implements ItemRepository {

    private final Map<Long, Item> items = new HashMap<>();

    @Override
    public Item saveItem(Item item) {
        item.setId(getNextId());
        items.put(item.getId(), item);
        return item;
    }

    @Override
    public Collection<Item> getItems() {
        return items.values();
    }

    @Override
    public Item getItemById(Long id) {
        return items.get(id);
    }

    @Override
    public Item updateItem(Item item, Long itemId) {
        Item newItem = items.get(itemId);
        if (item.getName() != null && !item.getName().isBlank()) newItem.setName(item.getName());
        if (item.getDescription() != null && !item.getDescription().isBlank())
            newItem.setDescription(item.getDescription());
        if (item.getAvailable() != null)
            newItem.setAvailable(item.getAvailable());
        items.put(itemId, newItem);
        return newItem;
    }

    @Override
    public void deleteItem(Long id) {
        items.remove(id);
    }


    private Long getNextId() {
        if (items.isEmpty()) return 1L;
        return items.values().stream()
                .mapToLong(Item::getId)
                .max().getAsLong() + 1;
    }
}
