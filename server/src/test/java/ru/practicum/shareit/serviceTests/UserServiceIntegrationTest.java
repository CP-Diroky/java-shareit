package ru.practicum.shareit.serviceTests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exceptions.DuplicatedDataEception;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserService;

import java.util.Collection;

@SpringBootTest
@Transactional
public class UserServiceIntegrationTest {


    @Autowired
    UserService userService;

    @Test
    void saveUserShouldReturnId() {
        User diyor = new User("Диёр", "diyorka255@mail.ru");

        userService.addUser(diyor);

        Assertions.assertNotNull(diyor.getId());
    }

    @Test
    void saveUserShouldReturnErrorWhenTwoEmails() {
        User diyor = new User("Диёр", "diyorka255@mail.ru");

        User diyorDuplicate = new User("ДиёрКлон", "diyorka255@mail.ru");

        userService.addUser(diyor);
        Assertions.assertThrows(DuplicatedDataEception.class, () -> userService.addUser(diyorDuplicate));
    }

    @Test
    void shouldGetAllUsers() {
        User diyor = new User("Диёр", "diyorka255@mail.ru");
        User donik = new User("Дониёр", "donikclonik2001@mail.ru");

        userService.addUser(diyor);
        userService.addUser(donik);

        Collection<User> userList = userService.getAllUsers();

        Assertions.assertEquals(2, userList.size());
    }

    @Test
    void shouldGetUserById() {
        User user = new User("Диёр", "mail@mail.ru");
        user = userService.addUser(user);

        User result = userService.getUserById(user.getId());

        Assertions.assertEquals("Диёр", result.getName());
    }

    @Test
    void shouldUpdateUser() {
        User diyor = new User("Диёр", "diyorka255@mail.ru");
        User saved = userService.addUser(diyor);

        saved.setEmail("diyorka2001@mail.ru");
        userService.updateUser(saved, saved.getId());

        User changed = userService.getUserById(saved.getId());

        Assertions.assertEquals("diyorka2001@mail.ru", changed.getEmail());
    }

    @Test
    void shouldDeleteUser() {
        User diyor = new User("Диёр", "diyorka255@mail.ru");
        userService.addUser(diyor);
        userService.deleteUser(diyor.getId());
        Assertions.assertEquals(0, userService.getAllUsers().size());
    }

    @Test
    void shouldThrowExceptionWhenUserNotFound() {
        Assertions.assertThrows(NotFoundException.class,
                () -> userService.getUserById(999L));
    }


}
