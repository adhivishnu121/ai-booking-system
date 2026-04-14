package com.adhi.aibooking.booking.exception;

import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class NoTableAvailableException extends RuntimeException {

    private final List<LocalDateTime> suggestedSlots;

    public NoTableAvailableException(String message,
                                     List<LocalDateTime> suggestedSlots) {
        super(message);
        this.suggestedSlots = suggestedSlots;
    }
}