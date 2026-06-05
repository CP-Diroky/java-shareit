package ru.practicum.shareit.controllerTests;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemController;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.item.comment.Comment;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(ItemController.class)
class ItemControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    ItemService itemService;

    @Test
    void shouldAddItem() throws Exception {

        Item item = new Item("PS4", "desc", true);
        item.setId(1L);

        when(itemService.addItem(any(), eq(1L), any())).thenReturn(item);

        mockMvc.perform(post("/items")
                        .header("X-Sharer-User-Id", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"PS4\",\"description\":\"desc\",\"available\":true}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void shouldUpdateItemPartially() throws Exception {

        Item item = new Item("PS4", "desc", true);
        item.setId(1L);

        when(itemService.updateItem(any(), eq(1L), eq(1L)))
                .thenReturn(item);

        mockMvc.perform(patch("/items/1")
                        .header("X-Sharer-User-Id", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"description\":\"updated desc\"}"))
                .andExpect(status().isOk());
    }

    @Test
    void shouldReturnEmptyItemsList() throws Exception {

        when(itemService.getItems(1L)).thenReturn(List.of());

        mockMvc.perform(get("/items")
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }


    @Test
    void shouldGetItem() throws Exception {

        ItemDto dto = new ItemDto(1L, "PS4", "desc", true, null);

        when(itemService.getItemById(1L, 1L)).thenReturn(dto);

        mockMvc.perform(get("/items/1")
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }


    @Test
    void shouldSearchItems() throws Exception {

        when(itemService.searchItems("ps")).thenReturn(List.of());

        mockMvc.perform(get("/items/search")
                        .param("text", "ps"))
                .andExpect(status().isOk());
    }

    @Test
    void shouldAddComment() throws Exception {

        User author = new User();
        author.setId(1L);
        author.setName("Диёр");

        Item item = new Item();
        item.setId(1L);

        Comment comment = new Comment();
        comment.setId(1L);
        comment.setText("Nice");
        comment.setAuthor(author);
        comment.setItem(item);
        comment.setCreated(LocalDateTime.now());

        when(itemService.addComment(any(), eq(1L), eq(1L)))
                .thenReturn(comment);

        mockMvc.perform(post("/items/1/comment")
                        .header("X-Sharer-User-Id", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"text\":\"Nice\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.text").value("Nice"))
                .andExpect(jsonPath("$.itemId").value(1))
                .andExpect(jsonPath("$.authorId").value(1))
                .andExpect(jsonPath("$.authorName").value("Диёр"));
    }

    @Test
    void shouldReturnEmptySearch() throws Exception {

        when(itemService.searchItems("nothing")).thenReturn(List.of());

        mockMvc.perform(get("/items/search")
                        .param("text", "nothing"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }


}


