package ru.practicum.shareit.request;

import jakarta.validation.constraints.Positive;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.dto.RequestMapper;

import java.util.Collection;


@RestController
@RequestMapping(path = "/requests")
public class RequestController {

    private final RequestService requestService;

    @Autowired
    public RequestController(RequestService requestService) {
        this.requestService = requestService;
    }

    @PostMapping
    public RequestDto addRequest(@RequestBody RequestDto requestDto,
                                 @RequestHeader("X-Sharer-User-Id") @Positive Long userId) {
        return RequestMapper.toRequestDto(requestService.addRequest(RequestMapper.toRequest(requestDto), userId));
    }

    @GetMapping
    public Collection<RequestDto> getRequests(@RequestHeader("X-Sharer-User-Id") @Positive Long userId) {
        return requestService.getRequests(userId);
    }

    @GetMapping("/all")
    public Collection<RequestDto> getAllRequests() {
        return requestService.getAllRequests();
    }

    @GetMapping("/{requestId}")
    public RequestDto getRequestById(@PathVariable @Positive Long requestId) {
        return requestService.getRequestById(requestId);
    }
}
