package ru.practicum.shareit.serviceTests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.item.comment.Comment;
import ru.practicum.shareit.item.comment.CommentRepository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserService;

import java.time.LocalDateTime;
import java.util.Collection;

@SpringBootTest
@Transactional
public class ItemServiceIntegrationTest {

    @Autowired
    ItemService itemService;

    @Autowired
    UserService userService;

    @Autowired
    BookingRepository bookingRepository;

    @Autowired
    CommentRepository commentRepository;

    @Test
    void shouldAddItem() {
        User diyor = userService.addUser(new User("Диёр", "mail@mail.ru"));

        Item item = new Item("Playstation 4", "Playstation 4 в хорошем состоянии, покупал в 2017 году",
                true);

        itemService.addItem(item, diyor.getId(), null);

        Assertions.assertEquals(1, itemService.getItems(diyor.getId()).size());
    }

    @Test
    void shouldNotAddItem() {
        Item item = new Item("Playstation 4", "Playstation 4 в хорошем состоянии, покупал в 2017 году",
                true);

        Assertions.assertThrows(NotFoundException.class, () -> itemService.addItem(item, 1L, null));

    }

    @Test
    void shouldUpdateItem() {
        User diyor = userService.addUser(new User("Диёр", "mail@mail.ru"));

        Item item = new Item("Playstation 4", "Playstation 4 в хорошем состоянии, покупал в 2017 году",
                true);

        itemService.addItem(item, diyor.getId(), null);

        item.setDescription("PlayStation 4, есть царапины на корпусе");

        itemService.updateItem(item, diyor.getId(), item.getId());

        Assertions.assertEquals("PlayStation 4, есть царапины на корпусе",
                itemService.getItemById(item.getId(), diyor.getId()).getDescription());
    }

    @Test
    void shouldNotUpdateItem() {
        User diyor = userService.addUser(new User("Диёр", "mail@mail.ru"));

        Item item = new Item("Playstation 4", "Playstation 4 в хорошем состоянии, покупал в 2017 году",
                true);

        itemService.addItem(item, diyor.getId(), null);

        item.setDescription("PlayStation 4, есть царапины на корпусе");

        Assertions.assertThrows(NotFoundException.class, () -> itemService.updateItem(item, 5L, item.getId()));
    }

    @Test
    void shouldReturnItemWithBookingsForOwner() {
        User owner = userService.addUser(new User("Диёр", "diyorka255@mail.com"));
        User booker = userService.addUser(new User("Александр", "Alexander@mail.com"));

        Item item = new Item("PS4", "хорошее состояние", true);
        item.setOwner(owner);
        item = itemService.addItem(item, owner.getId(), null);

        LocalDateTime now = LocalDateTime.now();

        Booking past = new Booking(
                now.minusDays(5),
                now.minusDays(1),
                item,
                booker
        );
        past.setStatus(Booking.Status.APPROVED);
        bookingRepository.save(past);

        Booking future = new Booking(
                now.plusDays(1),
                now.plusDays(5),
                item,
                booker
        );
        future.setStatus(Booking.Status.APPROVED);
        bookingRepository.save(future);

        ItemDto result = itemService.getItemById(item.getId(), owner.getId());

        Assertions.assertNotNull(result.getLastBooking());
        Assertions.assertNotNull(result.getNextBooking());
    }

    @Test
    void shouldHideBookingsForNotOwner() {
        User owner = userService.addUser(new User("Диёр", "diyorka255@mail.com"));
        User booker = userService.addUser(new User("Александр", "Alexander@mail.com"));

        Item item = new Item("PS4", "хорошее состояние", true);
        item.setOwner(owner);
        item = itemService.addItem(item, owner.getId(), null);

        Booking booking = new Booking(
                LocalDateTime.now().minusDays(3),
                LocalDateTime.now().minusDays(1),
                item,
                booker
        );
        booking.setStatus(Booking.Status.APPROVED);
        bookingRepository.save(booking);

        ItemDto result = itemService.getItemById(item.getId(), booker.getId());

        Assertions.assertNull(result.getLastBooking());
        Assertions.assertNull(result.getNextBooking());
    }

    @Test
    void shouldReturnItemsWithComments() {
        User owner = userService.addUser(new User("Диёр", "diyorka255@mail.com"));
        User booker = userService.addUser(new User("Александр", "Alexander@mail.com"));

        Item item = new Item("PS4", "хорошее состояние", true);
        item.setOwner(owner);
        item = itemService.addItem(item, owner.getId(), null);

        Booking booking = new Booking(
                LocalDateTime.now().minusDays(3),
                LocalDateTime.now().minusDays(1),
                item,
                booker
        );
        booking.setStatus(Booking.Status.APPROVED);
        bookingRepository.save(booking);

        Comment comment = new Comment();
        comment.setText("Nice item");
        comment.setItem(item);
        comment.setAuthor(booker);
        commentRepository.save(comment);

        Collection<ItemDto> result = itemService.getItems(owner.getId());

        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals(1, result.iterator().next().getComments().size());
    }

    @Test
    void shouldThrowIfUserNotFound() {
        Assertions.assertThrows(NotFoundException.class, () -> itemService.getItems(999L));
    }

    @Test
    void shouldReturnEmptyWhenBlankSearch() {
        Collection<Item> result = itemService.searchItems("   ");
        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    void shouldSearchItems() {
        User owner = userService.addUser(new User("Диёр", "diyorka255@mail.ru"));

        Item item = new Item("Playstation4", "хорошее состояние", true);
        item.setOwner(owner);
        itemService.addItem(item, owner.getId(), null);
        Collection<Item> result = itemService.searchItems("play");

        Assertions.assertFalse(result.isEmpty());
    }

    @Test
    void shouldAddComment() {
        User owner = userService.addUser(new User("Диёр", "diyorka255@mail.com"));
        User booker = userService.addUser(new User("Александр", "Alexander@mail.com"));

        Item item = new Item("PS4", "хорошее состояние", true);
        item.setOwner(owner);
        item = itemService.addItem(item, owner.getId(), null);

        Booking booking = new Booking(
                LocalDateTime.now().minusDays(3),
                LocalDateTime.now().minusDays(1),
                item,
                booker
        );
        booking.setStatus(Booking.Status.APPROVED);
        bookingRepository.save(booking);

        Comment comment = new Comment();
        comment.setText("Great!");

        Comment result = itemService.addComment(comment, item.getId(), booker.getId());

        Assertions.assertEquals("Great!", result.getText());
    }


}
