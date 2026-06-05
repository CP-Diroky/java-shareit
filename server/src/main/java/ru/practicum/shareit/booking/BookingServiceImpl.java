package ru.practicum.shareit.booking;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.exceptions.ConditionsNotMetException;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.Collection;

@Service
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Autowired
    public BookingServiceImpl(BookingRepository bookingRepository, ItemRepository itemRepository,
                              UserRepository userRepository) {
        this.bookingRepository = bookingRepository;
        this.itemRepository = itemRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    @Override
    public Booking addBooking(BookingDto bookingDto, Long userId) {
        LocalDateTime now = LocalDateTime.now();
        if (bookingDto == null) throw new ConditionsNotMetException("Неверный ввод!");
        LocalDateTime start = bookingDto.getStart();
        LocalDateTime end = bookingDto.getEnd();
        if (start == null || end == null)
            throw new ConditionsNotMetException("Даты бронирования не указаны!");
        else if (start.isBefore(now) || end.isBefore(now))
            throw new ConditionsNotMetException("Даты не могут быть в прошлом!");
        else if (start.equals(end)) throw new ConditionsNotMetException("Даты не могут быть одинаковыми!");
        Item item = itemRepository.findById(bookingDto.getItemId())
                .orElseThrow(() -> new NotFoundException("Вещь не найдена!"));
        User booker = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден!"));
        if (!item.getAvailable()) throw new ConditionsNotMetException("Вещь не доступна для бронирования!");
        Booking booking = new Booking(start, end, item, booker);
        booking.setStatus(Booking.Status.WAITING);
        return bookingRepository.save(booking);
    }

    @Transactional
    @Override
    public Booking approveBooking(Long bookingId, boolean approved, Long userId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() ->  new NotFoundException("Бронирование не найдено!"));
        if (!booking.getItem().getOwner().getId().equals(userId))
            throw new ConditionsNotMetException("Только владелец вещи может одобрить бронирование!");
        if (approved) booking.setStatus(Booking.Status.APPROVED);
        else booking.setStatus(Booking.Status.REJECTED);
        return bookingRepository.save(booking);
    }

    @Override
    public Booking getBookingById(Long bookingId, Long userId) {
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(() ->
                new NotFoundException("Бронирование не найдено!"));
        if (userRepository.findById(userId).isEmpty()) throw new NotFoundException("Пользователь не найден!");
        if (!booking.getItem().getOwner().getId().equals(userId) && !booking.getBooker().getId().equals(userId))
            throw new ConditionsNotMetException("Только владелец вещи или автор бронирования может получить информацию!");
        return booking;
    }

    @Override
    public Collection<Booking> getUsersBookings(String state, Long userId) {
        if (userRepository.findById(userId).isEmpty()) throw new NotFoundException("Пользователь не найден!");
        return switch (state) {
            case "CURRENT" -> bookingRepository.getCurrentBookings(userId);
            case "FUTURE" -> bookingRepository.getFutureBookings(userId);
            case "PAST" -> bookingRepository.getPastBookings(userId);
            case "WAITING", "REJECTED" -> bookingRepository.getBookingsByStatus(state, userId);
            default -> bookingRepository.findByBookerIdOrderByStartDesc(userId);
        };
    }

    @Override
    public Collection<Booking> getOwnersBookings(String state, Long userId) {
        if (userRepository.findById(userId).isEmpty()) throw new NotFoundException("Пользователь не найден!");
        return switch (state) {
            case "CURRENT" -> bookingRepository.getOwnerCurrentBookings(userId);
            case "FUTURE" -> bookingRepository.getOwnerFutureBookings(userId);
            case "PAST" -> bookingRepository.getOwnerPastBookings(userId);
            case "WAITING", "REJECTED" -> bookingRepository.getOwnerBookingsByStatus(Booking.Status.valueOf(state), userId);
            default -> bookingRepository.findByItemOwnerIdOrderByStartDesc(userId);
        };
    }

}
