package com.adhi.aibooking.booking.service;

import com.adhi.aibooking.booking.dto.HourPrediction;
import com.adhi.aibooking.booking.entity.Reservation;
import com.adhi.aibooking.booking.entity.RestaurantTable;
import com.adhi.aibooking.booking.exception.NoTableAvailableException;
import com.adhi.aibooking.booking.repository.ReservationRepository;
import com.adhi.aibooking.booking.repository.RestaurantTableRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final RestaurantTableRepository restaurantTableRepository;

    public ReservationService(ReservationRepository reservationRepository,
                              RestaurantTableRepository restaurantTableRepository) {
        this.reservationRepository = reservationRepository;
        this.restaurantTableRepository = restaurantTableRepository;
    }

    public Reservation createReservation(Reservation reservation, String restaurantId) {

        List<RestaurantTable> tables =
                restaurantTableRepository.findByRestaurantId(restaurantId);

        if (tables.isEmpty()) {
            throw new RuntimeException("No tables available in this restaurant");
        }

        RestaurantTable bestTable = tables.stream()
                .filter(t -> t.getCapacity() >= reservation.getPartySize())
                .sorted(Comparator.comparingInt(RestaurantTable::getCapacity))
                .filter(table -> {

                    List<Reservation> conflicts =
                            reservationRepository.findByTableIdAndBookingTime(
                                    table.getId(),
                                    reservation.getBookingTime()
                            );

                    return conflicts.isEmpty();
                })
                .findFirst()
                .orElse(null);

        if (bestTable == null) {
            List<LocalDateTime> alternatives =
                    findAlternativeSlots(restaurantId, reservation);

            throw new NoTableAvailableException(
            	    "No table available",
            	    alternatives
            	);
        }


        reservation.setTable(bestTable);
        reservation.setStatus("CONFIRMED");

        return reservationRepository.save(reservation);
    }
    private List<LocalDateTime> findAlternativeSlots(String restaurantId,
            Reservation reservation) {
List<LocalDateTime> suggestions = new ArrayList<>();

for (int i = 1; i <= 3; i++) {
LocalDateTime nextSlot = reservation.getBookingTime().plusHours(i);

boolean available = restaurantTableRepository
.findByRestaurantId(restaurantId)
.stream()
.filter(t -> t.getCapacity() >= reservation.getPartySize())
.anyMatch(table ->
reservationRepository
.findByTableIdAndBookingTime(
   table.getId(),
   nextSlot)
.isEmpty());

if (available) {
suggestions.add(nextSlot);
}
}

return suggestions;
}
    public long getTotalBookings() {
        return reservationRepository.getTotalBookings();
    }
    public long getTotalCovers() {
        return reservationRepository.getTotalCovers();
    }
    public double getAveragePartySize() {
        return reservationRepository.getAveragePartySize();
    }

    public List<Object[]> getPeakHours() {
        return reservationRepository.getPeakHours();
    }
    
    public List<HourPrediction> predictBusyHours() {

        List<Object[]> peakData = reservationRepository.getPeakHours();

        long max = peakData.stream()
                .mapToLong(o -> ((Number) o[1]).longValue())
                .max()
                .orElse(1);

        List<HourPrediction> result = new ArrayList<>();

        for (Object[] row : peakData) {
            int hour = ((Number) row[0]).intValue();
            long count = ((Number) row[1]).longValue();

            double score = (double) count / max;

            result.add(new HourPrediction(hour, score));
        }

        return result;
    }
    
    
    
    
    
    
    
    
    public List<Reservation> getAllReservations() {
        return reservationRepository.findAll();
    }

    public Reservation getById(String id) {
        return reservationRepository.findById(id).orElse(null);
    }

    public void deleteReservation(String id) {
        reservationRepository.deleteById(id);
    }
}