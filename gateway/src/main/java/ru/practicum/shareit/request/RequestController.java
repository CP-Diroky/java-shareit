package ru.practicum.shareit.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.RequestDto;


@RestController
@RequestMapping(path = "/requests")
@Validated
@RequiredArgsConstructor
public class RequestController {

    private final RequestClient requestClient;



    @PostMapping
    public ResponseEntity<Object> addRequest(@RequestBody @Valid RequestDto requestDto,
                                     @RequestHeader("X-Sharer-User-Id") @Positive Long userId) {
        return requestClient.addRequest(requestDto, userId);
    }

    @GetMapping
    public ResponseEntity<Object> getRequests(@RequestHeader("X-Sharer-User-Id") @Positive Long userId) {
        return requestClient.getRequests(userId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getAllRequests() {
        return requestClient.getAllRequests();
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> getRequestById(@PathVariable @Positive Long requestId) {
        return requestClient.getRequestById(requestId);
    }
}

