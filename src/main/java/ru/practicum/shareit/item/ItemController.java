package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;

import java.util.Collection;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequestMapping("/items")
@Validated
public class ItemController {

    private final ItemService itemService;

    @Autowired
    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }


    @PostMapping
    public Item addItem(@RequestBody @Valid ItemDto itemDto,
                           @RequestHeader("X-Sharer-User-Id") @Positive Long userId) {
        return itemService.addItem(ItemMapper.toItem(itemDto), userId);
    }

    @PatchMapping("/{itemId}")
    public Item updateItem(@RequestBody ItemDto itemDto, @PathVariable @Positive Long itemId,
                           @RequestHeader("X-Sharer-User-Id") @Positive Long userId) {
        return itemService.updateItem(ItemMapper.toItem(itemDto), userId, itemId);

    }

    @GetMapping("/{itemId}")
    public Item getItemById(@PathVariable @Positive Long itemId) {
        return itemService.getItemById(itemId);
    }

    @GetMapping
    public Collection<Item> getItems(@RequestHeader("X-Sharer-User-Id") @Positive Long userId) {
        return itemService.getItems(userId);
    }

    @GetMapping("/search")
    public Collection<Item> searchItems(@RequestParam String text) {
        return itemService.searchItems(text);
    }



}
