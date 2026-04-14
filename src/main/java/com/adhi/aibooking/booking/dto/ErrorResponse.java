package com.adhi.aibooking.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
public class ErrorResponse {
    private String message;
    private List<LocalDateTime> suggestedSlots;
}