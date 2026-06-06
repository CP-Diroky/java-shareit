package ru.practicum.shareit.serviceTests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.BookingService;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.exceptions.ConditionsNotMetException;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.Collection;

@SpringBootTest
@Transactional
class BookingServiceIntegrationTest {

    @Autowired
    BookingService bookingService;
    @Autowired
    BookingRepository bookingRepository;
    @Autowired
    ItemRepository itemRepository;
    @Autowired
    UserRepository userRepository;

    @Test
    void shouldAddBooking() {
        User owner = userRepository.save(new User("Диёр", "diyorka255@mail.com"));
        User booker = userRepository.save(new User("Booker", "booker@mail.com"));

        Item item = new Item("PS4", "Хорошее состояние", true);
        item.setOwner(owner);
        item = itemRepository.save(item);

        BookingDto dto = new BookingDto(
                null,
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(3),
                item.getId(),
                null,
                null
        );

        Booking result = bookingService.addBooking(dto, booker.getId());

        Assertions.assertEquals(Booking.Status.WAITING, result.getStatus());
        Assertions.assertNotNull(result.getId());
    }

    @Test
    void shouldThrowIfItemNotAvailable() {
        User owner = userRepository.save(new User("Диёр", "diyorka255@mail.com"));
        User booker = userRepository.save(new User("Booker", "booker@mail.com"));

        Item item = new Item("PS4", "Хорошее состояние", false);
        item.setOwner(owner);
        item = itemRepository.save(item);

        BookingDto dto = new BookingDto(
                null,
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(3),
                item.getId(),
                null,
                null
        );

        Assertions.assertThrows(ConditionsNotMetException.class,
                () -> bookingService.addBooking(dto, booker.getId()));
    }

    @Test
    void shouldCreateBookingEvenIfDatesInvalidBecauseGatewayValidates() {
        User owner = userRepository.save(new User("Диёр", "diyorka@mail.com"));
        User booker = userRepository.save(new User("Booker", "booker@mail.com"));

        Item item = new Item("PS4", "desc", true);
        item.setOwner(owner);
        item = itemRepository.save(item);

        BookingDto dto = new BookingDto(
                null,
                LocalDateTime.now().minusDays(5),
                LocalDateTime.now().minusDays(3),
                item.getId(),
                null,
                null
        );

        Booking result = bookingService.addBooking(dto, booker.getId());

        Assertions.assertNotNull(result);
        Assertions.assertEquals(Booking.Status.WAITING, result.getStatus());
    }

    @Test
    void shouldApproveBooking() {
        User owner = userRepository.save(new User("Диёр", "diyorka255@mail.com"));
        User booker = userRepository.save(new User("Booker", "booker@mail.com"));

        Item item = new Item("PS4", "Хорошее состояние", true);
        item.setOwner(owner);
        item = itemRepository.save(item);

        Booking booking = new Booking(
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(3),
                item,
                booker
        );
        booking.setStatus(Booking.Status.WAITING);
        booking = bookingRepository.save(booking);

        Booking result = bookingService.approveBooking(booking.getId(), true, owner.getId());

        Assertions.assertEquals(Booking.Status.APPROVED, result.getStatus());
    }

    @Test
    void shouldNotApproveIfNotOwner() {
        User owner = userRepository.save(new User("Диёр", "diyorka255@mail.com"));
        User booker = userRepository.save(new User("Booker", "booker@mail.com"));
        User other = userRepository.save(new User("Other", "other@mail.com"));

        Item item = new Item("PS4", "Хорошее состояние", true);
        item.setOwner(owner);
        item = itemRepository.save(item);

        Booking booking = new Booking(
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(3),
                item,
                booker
        );
        booking.setStatus(Booking.Status.WAITING);
        booking = bookingRepository.save(booking);
        Long bookingId = booking.getId();

        Assertions.assertThrows(ConditionsNotMetException.class,
                () -> bookingService.approveBooking(bookingId, true, other.getId()));
    }

    @Test
    void ownerCanGetBooking() {
        User owner = userRepository.save(new User("Диёр", "diyorka255@mail.com"));
        User booker = userRepository.save(new User("Booker", "booker@mail.com"));

        Item item = new Item("PS4", "Хорошее состояние", true);
        item.setOwner(owner);
        item = itemRepository.save(item);

        Booking booking = new Booking(
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(3),
                item,
                booker
        );
        booking.setStatus(Booking.Status.WAITING);
        booking = bookingRepository.save(booking);

        Booking result = bookingService.getBookingById(booking.getId(), owner.getId());

        Assertions.assertEquals(booking.getId(), result.getId());
    }

    @Test
    void shouldThrowIfNotOwnerOrBooker() {
        User owner = userRepository.save(new User("Диёр", "diyorka255@mail.com"));
        User booker = userRepository.save(new User("Booker", "booker@mail.com"));
        User other = userRepository.save(new User("Other", "other@mail.com"));

        Item item = new Item("PS4", "Хорошее состояние", true);
        item.setOwner(owner);
        item = itemRepository.save(item);

        Booking booking = new Booking(
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(3),
                item,
                booker
        );
        booking.setStatus(Booking.Status.WAITING);
        booking = bookingRepository.save(booking);
        Long bookingId = booking.getId();

        Assertions.assertThrows(ConditionsNotMetException.class,
                () -> bookingService.getBookingById(bookingId, other.getId()));
    }

    @Test
    void shouldReturnAllUserBookings() {
        User owner = userRepository.save(new User("Диёр", "diyorka255@mail.com"));
        User booker = userRepository.save(new User("Booker", "booker@mail.com"));

        Item item = new Item("PS4", "Хорошее состояние", true);
        item.setOwner(owner);
        item = itemRepository.save(item);

        Booking booking = new Booking(
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(3),
                item,
                booker
        );
        booking.setStatus(Booking.Status.WAITING);
        bookingRepository.save(booking);

        Collection<Booking> result =
                bookingService.getUsersBookings("ALL", booker.getId());

        Assertions.assertFalse(result.isEmpty());
    }

    @Test
    void shouldReturnOwnerWaitingBookings() {
        User owner = userRepository.save(new User("Диёр", "diyorka255@mail.com"));
        User booker = userRepository.save(new User("Booker", "booker@mail.com"));

        Item item = new Item("PS4", "Хорошее состояние", true);
        item.setOwner(owner);
        item = itemRepository.save(item);

        Booking booking = new Booking(
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(3),
                item,
                booker
        );
        booking.setStatus(Booking.Status.WAITING);
        bookingRepository.save(booking);

        Collection<Booking> result = bookingService.getOwnersBookings("WAITING", owner.getId());

        Assertions.assertFalse(result.isEmpty());
    }

    @Test
    void shouldThrowWhenBookingNotFound() {
        Assertions.assertThrows(NotFoundException.class,
                () -> bookingService.getBookingById(999L, 1L));
    }
}
