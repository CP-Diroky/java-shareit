package ru.practicum.shareit.request;

import ru.practicum.shareit.request.dto.RequestDto;

import java.util.Collection;

public interface RequestService {

    Request addRequest(Request request, Long userId);

    Collection<RequestDto> getRequests(Long userId);

    Collection<RequestDto> getAllRequests();

    RequestDto getRequestById(Long requestId);
}
