package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;


@Validated
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {

    private final ItemClient itemClient;


    @PostMapping
    public ResponseEntity<Object> addItem(@RequestBody @Valid ItemDto itemDto,
                                           @RequestHeader("X-Sharer-User-Id") @Positive Long userId) {
        return itemClient.addItem(itemDto, userId);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> updateItem(@RequestBody @Valid ItemDto itemDto, @PathVariable @Positive Long itemId,
                              @RequestHeader("X-Sharer-User-Id") @Positive Long userId) {
        return itemClient.updateItem(itemDto,userId, itemId);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> getItemById(@PathVariable @Positive Long itemId,
                               @RequestHeader("X-Sharer-User-Id") @Positive Long userId) {
        return itemClient.getItemById(itemId,userId);
    }

    @GetMapping
    public ResponseEntity<Object> getItems(@RequestHeader("X-Sharer-User-Id") @Positive Long userId) {
        return itemClient.getItems(userId);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> searchItems(@RequestParam String text) {
        return itemClient.searchItems(text);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> addComment(@RequestBody @Valid CommentDto commentDto, @PathVariable @Positive Long itemId,
                                             @RequestHeader("X-Sharer-User-Id") @Positive Long userId) {
        return itemClient.addComment(commentDto, itemId, userId);
    }


}
