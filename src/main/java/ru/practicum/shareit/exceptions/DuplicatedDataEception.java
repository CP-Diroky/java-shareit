package ru.practicum.shareit.exceptions;

public class DuplicatedDataEception extends RuntimeException {
    public DuplicatedDataEception(String message) {
        super(message);
    }
}
