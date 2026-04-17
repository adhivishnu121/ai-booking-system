package com.adhi.aibooking.booking.controller;

import com.adhi.aibooking.booking.entity.Reservation;
import com.adhi.aibooking.booking.service.ReservationService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@CrossOrigin(origins = "http://localhost:3000")

@RestController
@RequestMapping("/reservations")
public class ReservationController {

    private final ReservationService service;

    public ReservationController(ReservationService service) {
        this.service = service;
    }

    @PostMapping("/{restaurantId}")
    public Reservation create(@PathVariable String restaurantId,
                              @RequestBody Reservation reservation) {
        return service.createReservation(reservation, restaurantId);
    }
    @GetMapping("/by-date")
    public List<Reservation> getByDate(@RequestParam String date) {

        return service.getReservationsByDate(date);
    }
  

    @GetMapping("/{id}")
    public Reservation getById(@PathVariable String id) {
        return service.getById(id);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) {
        service.deleteReservation(id);
    }
}