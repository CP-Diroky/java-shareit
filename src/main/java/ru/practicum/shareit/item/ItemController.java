package ru.practicum.shareit.item;

import jakarta.validation.constraints.Positive;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.comment.dto.CommentDto;
import ru.practicum.shareit.item.comment.dto.CommentMapper;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;

import java.util.Collection;

@Validated
@RestController
@RequestMapping("/items")
public class ItemController {

    private final ItemService itemService;

    @Autowired
    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }


    @PostMapping
    public ItemDto addItem(@RequestBody ItemDto itemDto,
                           @RequestHeader("X-Sharer-User-Id") @Positive Long userId) {
        return ItemMapper.toItemDto(itemService.addItem(ItemMapper.toItem(itemDto), userId));
    }

    @PatchMapping("/{itemId}")
    public ItemDto updateItem(@RequestBody ItemDto itemDto, @PathVariable @Positive Long itemId,
                           @RequestHeader("X-Sharer-User-Id") @Positive Long userId) {
        return ItemMapper.toItemDto(itemService.updateItem(ItemMapper.toItem(itemDto), userId, itemId));

    }

    @GetMapping("/{itemId}")
    public ItemDto getItemById(@PathVariable @Positive Long itemId,
                               @RequestHeader("X-Sharer-User-Id") @Positive Long userId) {
        return itemService.getItemById(itemId, userId);
    }

    @GetMapping
    public Collection<ItemDto> getItems(@RequestHeader("X-Sharer-User-Id") @Positive Long userId) {
        return itemService.getItems(userId);
    }

    @GetMapping("/search")
    public Collection<ItemDto> searchItems(@RequestParam String text) {
        return ItemMapper.itemDtoCollection(itemService.searchItems(text));
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto addComment(@RequestBody CommentDto commentDto, @PathVariable @Positive Long itemId,
                                 @RequestHeader("X-Sharer-User-Id") @Positive Long userId) {
        return CommentMapper.toCommentDto(itemService.addComment(CommentMapper.toComment(commentDto),itemId,userId));
    }


}
