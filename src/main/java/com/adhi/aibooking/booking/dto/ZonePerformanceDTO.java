package com.adhi.aibooking.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ZonePerformanceDTO {
    private String zone;
    private long bookings;
}