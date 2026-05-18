package ru.practicum.shareit.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.ConditonsNotMetException;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.util.Collection;

@Service
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Autowired
    public ItemServiceImpl(ItemRepository itemRepository, UserRepository userRepository) {
        this.itemRepository = itemRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Item addItem(Item item, Long userId) {
        User owner = userRepository.getUserById(userId);
        if (owner == null) throw new NotFoundException("Пользователь не найден!");
        else if (item.getName() == null || item.getDescription() == null || item.getAvailable() == null) {
            throw new ConditonsNotMetException("Неверный ввод!");
        } else if (item.getName().isBlank() || item.getDescription().isBlank()) {
            throw new ConditonsNotMetException("Неверный ввод!");
        }
        item.setOwner(owner);
        return itemRepository.saveItem(item);
    }

    @Override
    public Item updateItem(Item item, Long userId, Long itemId) {
        if (userRepository.getUserById(userId) == null) throw new NotFoundException("Пользователь не найден!");
        else if (itemRepository.getItemById(itemId) == null) throw new NotFoundException("Вещь не найдена!");
        else if (!itemRepository.getItemById(itemId).getOwner().getId().equals(userId))
            throw new ConditonsNotMetException("Только владелец может поменять описание вещи!");
        return itemRepository.updateItem(item, itemId);
    }

    @Override
    public Item getItemById(Long itemId) {
        Item item = itemRepository.getItemById(itemId);
        if (item == null) throw new NotFoundException("Вещь не найдена!");
        return item;
    }

    @Override
    public Collection<Item> getItems(Long userId) {
        if (userRepository.getUserById(userId) == null) throw new NotFoundException("Пользователь не найден!");
        return itemRepository.getItems().stream()
                .filter(item -> item.getOwner().getId().equals(userId))
                .toList();
    }

    @Override
    public Collection<Item> searchItems(String text) {
        String query = text.toLowerCase();
        return itemRepository.getItems().stream()
                .filter(item -> Boolean.TRUE.equals(item.getAvailable()))
                .filter(item -> text != null && !text.isBlank() && (item.getName().toLowerCase().contains(query)
                        || item.getDescription().toLowerCase().contains(query)))
                .toList();
    }
}
