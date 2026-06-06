package ru.practicum.shareit.serviceTests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.request.Request;
import ru.practicum.shareit.request.RequestRepository;
import ru.practicum.shareit.request.RequestService;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.Collection;

@SpringBootTest
@Transactional
class RequestServiceIntegrationTest {

    @Autowired
    RequestService requestService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    RequestRepository requestRepository;
    @Autowired
    ItemRepository itemRepository;

    @Test
    void shouldAddRequest() {
        User user = userRepository.save(
                new User("Диёр", "diyorka255@mail.ru")
        );

        Request request = new Request("Need PS5");

        Request result = requestService.addRequest(request, user.getId());

        Assertions.assertNotNull(result.getId());
        Assertions.assertEquals("Need PS5", result.getDescription());
        Assertions.assertNotNull(result.getCreated());
    }

    @Test
    void shouldThrowIfUserNotFound() {
        Request request = new Request("Need laptop");

        Assertions.assertThrows(NotFoundException.class, () -> requestService.addRequest(request, 999L));
    }

    @Test
    void shouldReturnUserRequestsWithItems() {
        User user = userRepository.save(new User("Диёр", "diyorka255@mail.ru"));
        User owner = userRepository.save(new User("Дониёр", "donikclonik@mail.ru"));
        Request request = new Request("Need PS4");
        request.setRequestor(user);
        request.setCreated(LocalDateTime.now());
        request = requestRepository.save(request);

        Item item = new Item("PS4", "desc", true);
        item.setRequest(request);
        item.setOwner(owner);
        itemRepository.save(item);

        Collection<RequestDto> result = requestService.getRequests(user.getId());

        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals("Need PS4", result.iterator().next().getDescription());
        Assertions.assertEquals(1, result.iterator().next().getItems().size());
    }

    @Test
    void shouldReturnEmptyItemsIfNoItems() {
        User user = userRepository.save(new User("Диёр", "diyorka255@mail.ru"));

        Request request = new Request("Need PS4");
        request.setRequestor(user);
        request.setCreated(LocalDateTime.now());
        requestRepository.save(request);

        Collection<RequestDto> result = requestService.getRequests(user.getId());

        Assertions.assertEquals(1, result.size());
        Assertions.assertTrue(result.iterator().next().getItems().isEmpty());
    }

    @Test
    void shouldReturnAllRequests() {
        User user1 = userRepository.save(new User("Диёр", "diyorka255@mail.ru"));

        User user2 = userRepository.save(new User("Дониёр", "donikclonik@mail.ru"));

        Request r1 = new Request("Need PS4");
        r1.setRequestor(user1);
        r1.setCreated(LocalDateTime.now());
        requestRepository.save(r1);

        Request r2 = new Request("Need Laptop");
        r2.setRequestor(user2);
        r2.setCreated(LocalDateTime.now());
        requestRepository.save(r2);

        Collection<RequestDto> result = requestService.getAllRequests();

        Assertions.assertEquals(2, result.size());
    }

    @Test
    void shouldGetRequestByIdWithItems() {
        User user = userRepository.save(new User("Диёр", "diyorka255@mail.ru"));
        User owner = userRepository.save(new User("Дониёр", "donikclonik@mail.com"));
        Request request = new Request("Need PS4");
        request.setRequestor(user);
        request.setCreated(LocalDateTime.now());
        request = requestRepository.save(request);

        Item item = new Item("PS4", "desc", true);
        item.setRequest(request);
        item.setOwner(owner);
        itemRepository.save(item);

        RequestDto result = requestService.getRequestById(request.getId());

        Assertions.assertEquals("Need PS4", result.getDescription());
        Assertions.assertEquals(1, result.getItems().size());
    }

    @Test
    void shouldThrowIfRequestNotFound() {
        Assertions.assertThrows(NotFoundException.class, () -> requestService.getRequestById(999L));
    }
}
