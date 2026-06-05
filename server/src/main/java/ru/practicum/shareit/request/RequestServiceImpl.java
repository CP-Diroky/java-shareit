package ru.practicum.shareit.request;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exceptions.ConditionsNotMetException;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.dto.RequestMapper;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class RequestServiceImpl implements RequestService {

    private final RequestRepository requestRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    @Autowired
    public RequestServiceImpl(RequestRepository requestRepository, UserRepository userRepository,
                              ItemRepository itemRepository) {
        this.requestRepository = requestRepository;
        this.userRepository = userRepository;
        this.itemRepository = itemRepository;
    }


    @Transactional
    @Override
    public Request addRequest(Request request, Long userId) {
        if (request.getDescription() == null || request.getDescription().isBlank()) {
            throw new ConditionsNotMetException("Описание не должно быть пустым!");
        }
        request.setCreated(LocalDateTime.now());
        User requestor = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Пользователь не найден!"));
        request.setRequestor(requestor);
        return requestRepository.save(request);
    }

    @Override
    public Collection<RequestDto> getRequests(Long userId) {
        if (userRepository.findById(userId).isEmpty()) throw new NotFoundException("Пользователь не найден!");
        List<Request> requests = requestRepository.findByRequestorIdOrderByCreatedDesc(userId);
        if (requests.isEmpty()) return List.of();
        List<Long> requestsIds = requests.stream().map(Request::getId).toList();
        List<Item> items = itemRepository.findByRequestIdIn(requestsIds);
        List<ItemDto> responses;
        List<RequestDto> result = new ArrayList<>();
        for (Request r: requests) {
            responses = items.stream().filter(i -> i.getRequest().getId().equals(r.getId()))
                    .map(ItemMapper::toItemDtoWithOwnerId).toList();
            result.add(RequestMapper.toRequestDtoWithItems(r, responses));
        }
        return result;
    }

    @Override
    public Collection<RequestDto> getAllRequests() {
        List<Request> requests = requestRepository.findAll();
        if (requests.isEmpty()) return List.of();
        List<Long> requestsIds = requests.stream().map(Request::getId).toList();
        List<Item> items = itemRepository.findByRequestIdIn(requestsIds);
        List<ItemDto> responses;
        List<RequestDto> result = new ArrayList<>();
        for (Request r: requests) {
            responses = items.stream().filter(i -> i.getRequest().getId().equals(r.getId()))
                    .map(ItemMapper::toItemDtoWithOwnerId).toList();
            result.add(RequestMapper.toRequestDtoWithItems(r, responses));
        }
        return result;
    }

    @Override
    public RequestDto getRequestById(Long requestId) {
        Request request = requestRepository.findById(requestId).orElseThrow(() -> new NotFoundException("Запрос не найден!"));
        List<ItemDto> responses = itemRepository.findByRequestId(request.getId())
                .stream().map(ItemMapper::toItemDtoWithOwnerId).toList();
        return RequestMapper.toRequestDtoWithItems(request, responses);
    }



}
