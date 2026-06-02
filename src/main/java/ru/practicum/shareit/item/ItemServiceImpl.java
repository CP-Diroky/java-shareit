package ru.practicum.shareit.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.exceptions.ConditionsNotMetException;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.item.comment.Comment;
import ru.practicum.shareit.item.comment.CommentRepository;
import ru.practicum.shareit.item.comment.dto.CommentDto;
import ru.practicum.shareit.item.comment.dto.CommentMapper;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.request.Request;
import ru.practicum.shareit.request.RequestRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;
    private final RequestRepository requestRepository;

    @Autowired
    public ItemServiceImpl(ItemRepository itemRepository, UserRepository userRepository,
                           BookingRepository bookingRepository, CommentRepository commentRepository,
                           RequestRepository requestRepository) {
        this.itemRepository = itemRepository;
        this.userRepository = userRepository;
        this.bookingRepository = bookingRepository;
        this.commentRepository = commentRepository;
        this.requestRepository = requestRepository;
    }

    @Transactional
    @Override
    public Item addItem(Item item, Long userId, Long requestId) {
        User owner = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Пользователь не найден!"));
        if (requestId != null) {
            Request request = requestRepository.findById(requestId).orElse(null);
            item.setRequest(request);
        }
        if (item.getName() == null || item.getDescription() == null || item.getAvailable() == null) {
            throw new ConditionsNotMetException("Неверный ввод!");
        } else if (item.getName().isBlank() || item.getDescription().isBlank()) {
            throw new ConditionsNotMetException("Неверный ввод!");
        }
        item.setOwner(owner);
        return itemRepository.save(item);
    }

    @Transactional
    @Override
    public Item updateItem(Item item, Long userId, Long itemId) {
        Item newItem = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Вещь не найдена!"));
        if (userRepository.findById(userId).isEmpty()) throw new NotFoundException("Пользователь не найден!");
        else if (!newItem.getOwner().getId().equals(userId))
            throw new ConditionsNotMetException("Только владелец может поменять описание вещи!");
        if (item.getName() != null && !item.getName().isBlank()) newItem.setName(item.getName());
        if (item.getDescription() != null && !item.getDescription().isBlank())
            newItem.setDescription(item.getDescription());
        if (item.getAvailable() != null) newItem.setAvailable(item.getAvailable());
        return itemRepository.save(newItem);
    }

    @Override
    public ItemDto getItemById(Long itemId, Long userId) {
        Item item = itemRepository.findById(itemId).orElseThrow(() -> new NotFoundException("Вещь не найдена!"));
        List<Booking> bookings = bookingRepository.findByItemIdIn(List.of(itemId));
        LocalDateTime lastBooking;
        LocalDateTime nextBooking;
        LocalDateTime now = LocalDateTime.now();
        Collection<CommentDto> commentList = CommentMapper.toCommentDtoCollection(commentRepository.findByItemId(itemId));
        if (!item.getOwner().getId().equals(userId)) {
            lastBooking = null;
            nextBooking = null;
        } else {
            lastBooking = bookings.stream()
                    .map(Booking::getEnd)
                    .filter(end -> end.isBefore(now))
                    .max(LocalDateTime::compareTo).orElse(null);
            nextBooking = bookings.stream()
                    .map(Booking::getStart)
                    .filter(start -> start.isAfter(now))
                    .min(LocalDateTime::compareTo).orElse(null);
        }
        return ItemMapper.toItemDtoWithDatesAndComments(item, lastBooking, nextBooking, commentList);
    }

    @Override
    public Collection<ItemDto> getItems(Long userId) {
        if (userRepository.findById(userId).isEmpty()) throw new NotFoundException("Пользователь не найден!");
        Map<Long, Item> itemsAndId = new HashMap<>();
        List<Item> items = itemRepository.findByOwnerId(userId);
        if (items.isEmpty()) return List.of();
        for (Item i: items) {
            itemsAndId.put(i.getId(), i);
        }
        List<Booking> bookings = bookingRepository.findByItemIdIn(itemsAndId.keySet());
        LocalDateTime lastBooking;
        LocalDateTime nextBooking;
        LocalDateTime now = LocalDateTime.now();
        List<ItemDto> result = new ArrayList<>();
        Collection<CommentDto> commentList =
                CommentMapper.toCommentDtoCollection(commentRepository.findByItemIdIn(itemsAndId.keySet()));
        Collection<CommentDto> itemsComments;
        for (Long i: itemsAndId.keySet()) {
            lastBooking = bookings.stream()
                    .filter(booking -> booking.getItem().getId().equals(i))
                    .map(Booking::getEnd)
                    .filter(end -> end.isBefore(now))
                    .max(LocalDateTime::compareTo).orElse(null);
            nextBooking = bookings.stream()
                    .filter(booking -> booking.getItem().getId().equals(i))
                    .map(Booking::getStart)
                    .filter(start -> start.isAfter(now))
                    .min(LocalDateTime::compareTo).orElse(null);
            itemsComments = commentList.stream()
                    .filter(commentDto -> commentDto.getItemId().equals(i)).toList();
            result.add(ItemMapper.toItemDtoWithDatesAndComments(itemsAndId.get(i),
                    lastBooking, nextBooking, itemsComments));
        }
        return result;
    }

    @Override
    public Collection<Item> searchItems(String text) {
        if (text.isBlank()) return List.of();
        return itemRepository.searchItems(text);
    }

    @Transactional
    @Override
    public Comment addComment(Comment comment, Long itemId, Long userId) {
        if (!bookingRepository.existsByBookerIdAndItemIdAndEndBeforeAndStatus(userId, itemId,
                LocalDateTime.now(), Booking.Status.APPROVED))
            throw new ConditionsNotMetException("Условия для добавления комментария не выполнены");
        Item item = itemRepository.findById(itemId).orElseThrow(() -> new NotFoundException("Вещь не найдена!"));
        User author = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Пользователь не найден!"));
        comment.setItem(item);
        comment.setAuthor(author);
        return commentRepository.save(comment);
    }
}
