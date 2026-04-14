package com.adhi.aibooking.booking.controller;

import com.adhi.aibooking.booking.dto.HourPrediction;
import com.adhi.aibooking.booking.service.ReservationService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/analytics")
public class AnalyticsController {

    private final ReservationService service;

    public AnalyticsController(ReservationService service) {
        this.service = service;
    }

    @GetMapping("/total-bookings")
    public long totalBookings() {
        return service.getTotalBookings();
    }
    @GetMapping("/total-covers")
    public long totalCovers() {
        return service.getTotalCovers();
    }
    @GetMapping("/average-party-size")
    public double averagePartySize() {
        return service.getAveragePartySize();
    }

    @GetMapping("/peak-hours")
    public List<Object[]> peakHours() {
        return service.getPeakHours();
    }
    

    @GetMapping("/predict-busy-hours")
    public List<HourPrediction> predictBusyHours() {
        return service.predictBusyHours();
    }
}