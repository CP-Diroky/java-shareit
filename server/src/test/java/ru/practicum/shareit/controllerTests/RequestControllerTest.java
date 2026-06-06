package ru.practicum.shareit.controllerTests;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.request.Request;
import ru.practicum.shareit.request.RequestController;
import ru.practicum.shareit.request.RequestService;
import ru.practicum.shareit.request.dto.RequestDto;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RequestController.class)
class RequestControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    RequestService requestService;

    @Test
    void shouldAddRequest() throws Exception {

        Request request = new Request("Need PS4");
        request.setId(1L);

        when(requestService.addRequest(any(), eq(1L))).thenReturn(request);

        mockMvc.perform(post("/requests")
                        .header("X-Sharer-User-Id", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"description\":\"Need PS4\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description").value("Need PS4"));
    }

    @Test
    void shouldGetRequests() throws Exception {

        when(requestService.getRequests(1L)).thenReturn(List.of());

        mockMvc.perform(get("/requests")
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk());
    }

    @Test
    void shouldGetRequestById() throws Exception {

        RequestDto dto = new RequestDto(1L, "Need PS4", LocalDateTime.now(), List.of());

        when(requestService.getRequestById(1L)).thenReturn(dto);

        mockMvc.perform(get("/requests/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void shouldGetAllRequests() throws Exception {

        when(requestService.getAllRequests()).thenReturn(List.of(
                new RequestDto(1L, "Need PS4", LocalDateTime.now(), List.of()),
                new RequestDto(2L, "Need Laptop", LocalDateTime.now(), List.of())));

        mockMvc.perform(get("/requests/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

}

