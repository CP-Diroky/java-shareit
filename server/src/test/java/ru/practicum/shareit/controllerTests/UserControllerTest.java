package ru.practicum.shareit.controllerTests;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserController;
import ru.practicum.shareit.user.UserService;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = UserController.class)
public class UserControllerTest {

    @MockBean
    UserService userService;


    @Autowired
    MockMvc mockMvc;

    @Test
    void shouldCreateUser() throws Exception {

        User user = new User(1L, "Диёр", "mail@mail.ru");

        when(userService.addUser(any())).thenReturn(user);

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"Диёр\",\"email\":\"mail@mail.ru\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Диёр"));
    }

    @Test
    void shouldGetAllUsers() throws Exception {

        List<User> users = List.of(
                new User(1L, "A", "a@mail.com"),
                new User(2L, "B", "b@mail.com")
        );

        when(userService.getAllUsers()).thenReturn(users);

        mockMvc.perform(get("/users")).andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void shouldGetUserById() throws Exception {

        User user = new User(1L, "Диёр", "mail@mail.ru");

        when(userService.getUserById(1L)).thenReturn(user);

        mockMvc.perform(get("/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void shouldUpdateUser() throws Exception {

        User updated = new User(1L, "New", "new@mail.com");

        when(userService.updateUser(any(), eq(1L))).thenReturn(updated);

        mockMvc.perform(patch("/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"New\",\"email\":\"newMail@mail.ru\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("New"));
    }

    @Test
    void shouldDeleteUser() throws Exception {

        doNothing().when(userService).deleteUser(1L);

        mockMvc.perform(delete("/users/1"))
                .andExpect(status().isNoContent());
    }

}
