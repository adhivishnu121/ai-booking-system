package com.adhi.aibooking.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class HourPrediction {
    private int hour;
    private double score;
}