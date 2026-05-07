package ru.practicum.shareit.exceptions;

public class ConditonsNotMetException extends RuntimeException {
    public ConditonsNotMetException(String message) {
        super(message);
    }
}
