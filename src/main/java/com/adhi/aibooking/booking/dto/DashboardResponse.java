package com.adhi.aibooking.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class DashboardResponse {

    private long totalBookings;
    private long totalCovers;
    private double averagePartySize;
    private double tableUtilization;

    private List<HourCountDTO> peakHours;
    private List<HourCountDTO> lowDemandHours;
    private List<TableTurnoverDTO> tableTurnover;
    private List<ZonePerformanceDTO> zonePerformance;
}