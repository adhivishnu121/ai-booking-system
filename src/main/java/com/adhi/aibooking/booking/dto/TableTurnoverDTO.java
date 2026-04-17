package com.adhi.aibooking.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TableTurnoverDTO {
    private String tableId;
    private long bookings;
}