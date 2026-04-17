package com.adhi.aibooking.booking.service;

import com.adhi.aibooking.booking.dto.DashboardResponse;
import com.adhi.aibooking.booking.dto.HourCountDTO;
import com.adhi.aibooking.booking.dto.HourPrediction;
import com.adhi.aibooking.booking.dto.TableTurnoverDTO;
import com.adhi.aibooking.booking.dto.ZonePerformanceDTO;
import com.adhi.aibooking.booking.entity.Reservation;
import com.adhi.aibooking.booking.entity.RestaurantTable;
import com.adhi.aibooking.booking.exception.NoTableAvailableException;
import com.adhi.aibooking.booking.repository.ReservationRepository;
import com.adhi.aibooking.booking.repository.RestaurantTableRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
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

        List<Object[]> peakHours = reservationRepository.getPeakHours();

        int hour = reservation.getBookingTime().getHour();
        double demandFactor = getDemandFactor(hour, peakHours);

        RestaurantTable bestTable = tables.stream()
                .filter(t -> "AVAILABLE".equalsIgnoreCase(t.getStatus()))
                .filter(t -> t.getCapacity() >= reservation.getPartySize())
                .filter(t -> reservation.getPreferredZone() == null ||
                        t.getZone().equalsIgnoreCase(reservation.getPreferredZone()))
                .filter(t -> !reservation.isPreferWindow() || t.isWindowSeat())
                .filter(t -> !reservation.isPreferVip() || t.isVip())
                .max((a, b) -> Integer.compare(
                        calculateScore(a, reservation, demandFactor),
                        calculateScore(b, reservation, demandFactor)
                ))
                .orElse(null);

        if (bestTable == null) {
            List<LocalDateTime> alternatives =
                    findAlternativeSlots(restaurantId, reservation);

            throw new NoTableAvailableException("No table available", alternatives);
        }

        reservation.setTable(bestTable);
        reservation.setStatus("CONFIRMED");

        return reservationRepository.save(reservation);
    }
    private double getDemandFactor(int hour, List<Object[]> peakHours) {

        long max = peakHours.stream()
                .mapToLong(o -> ((Number) o[1]).longValue())
                .max()
                .orElse(1);

        for (Object[] row : peakHours) {
            int h = ((Number) row[0]).intValue();
            long count = ((Number) row[1]).longValue();

            if (h == hour) {
                return (double) count / max;
            }
        }

        return 0.3;
    }
private int calculateScore(RestaurantTable table, Reservation reservation, double demandFactor) {

    int score = 0;

    if (table.getCapacity() == reservation.getPartySize()) {
        score += 20;
    } else {
        score += 5;
    }

    if (reservation.isPreferVip() && table.isVip()) {
        score += 25;
    }

    if (reservation.isPreferWindow() && table.isWindowSeat()) {
        score += 15;
    }

    if (reservation.getPreferredZone() != null &&
            reservation.getPreferredZone().equalsIgnoreCase(table.getZone())) {
        score += 12;
    }

    if (table.isMergeable()) {
        score += 5;
    }

    if (table.getCapacity() > reservation.getPartySize() + 2) {
        score -= 5;
    }

    if (demandFactor > 0.7 && table.getCapacity() <= reservation.getPartySize() + 1) {
        score += 10;
    }

    if (demandFactor > 0.7 && table.getCapacity() > reservation.getPartySize() + 3) {
        score -= 10;
    }

    return score;
}
    private List<LocalDateTime> findAlternativeSlots(String restaurantId,
                                                      Reservation reservation) {

        List<LocalDateTime> suggestions = new ArrayList<>();

        List<RestaurantTable> tables =
                restaurantTableRepository.findByRestaurantId(restaurantId);

        for (int i = 1; i <= 3; i++) {

            LocalDateTime nextSlot = reservation.getBookingTime().plusHours(i);

            boolean available = tables.stream()
                    .filter(t -> t.getCapacity() >= reservation.getPartySize())
                    .anyMatch(table ->
                            reservationRepository
                                    .findByTableIdAndBookingTime(table.getId(), nextSlot)
                                    .isEmpty()
                    );

            if (available) {
                suggestions.add(nextSlot);
            }
        }

        return suggestions;
    }
    public List<Reservation> getReservationsByDate(String date) {

        LocalDate selectedDate = LocalDate.parse(date);

        LocalDateTime start = selectedDate.atStartOfDay();
        LocalDateTime end = selectedDate.plusDays(1).atStartOfDay();

        return reservationRepository.findByBookingTimeBetween(start, end);
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
    public List<Object[]> getTableTurnover() {
        return reservationRepository.getTableTurnover();
    }
    public List<Object[]> getLowDemandHours() {
        return reservationRepository.getLowDemandHours();
    }
    public List<Reservation> getAllReservations() {
        return reservationRepository.findAll();
    }
   

    public Reservation getById(String id) {
        return reservationRepository.findById(id).orElse(null);
    }
    public double getTableUtilization() {
        return reservationRepository.getTableUtilization();
    }
    public void deleteReservation(String id) {
        reservationRepository.deleteById(id);
    }
    public DashboardResponse getDashboard(String date) {
		
		 LocalDate selectedDate = (date == null || date.isEmpty()) ? LocalDate.now() :
		 LocalDate.parse(date);
        LocalDateTime start = selectedDate.atStartOfDay();
        LocalDateTime end = selectedDate.plusDays(1).atStartOfDay();

        long totalBookings =
                reservationRepository.getTotalBookingsByDateRange(start, end);

        long totalCovers =
                reservationRepository.getTotalCoversByDateRange(start, end);

        double avgPartySize =
                reservationRepository.getAveragePartySizeByDateRange(start, end);

        double tableUtilization =
                reservationRepository.getTableUtilizationByDateRange(start, end);

        List<HourCountDTO> peakHours =
                reservationRepository.getPeakHoursByDateRange(start, end)
                        .stream()
                        .map(o -> new HourCountDTO(
                                ((Number) o[0]).intValue(),
                                ((Number) o[1]).longValue()
                        ))
                        .toList();

        List<HourCountDTO> lowDemandHours =
                reservationRepository.getLowDemandHoursByDateRange(start, end)
                        .stream()
                        .map(o -> new HourCountDTO(
                                ((Number) o[0]).intValue(),
                                ((Number) o[1]).longValue()
                        ))
                        .toList();

        List<TableTurnoverDTO> turnover =
                reservationRepository.getTableTurnoverByDateRange(start, end)
                        .stream()
                        .map(o -> new TableTurnoverDTO(
                                (String) o[0],
                                ((Number) o[1]).longValue()
                        ))
                        .toList();

        List<ZonePerformanceDTO> zonePerformance =
                reservationRepository.getZonePerformanceByDateRange(start, end)
                        .stream()
                        .map(o -> new ZonePerformanceDTO(
                                (String) o[0],
                                ((Number) o[1]).longValue()
                        ))
                        .toList();

        return new DashboardResponse(
                totalBookings,
                totalCovers,
                avgPartySize,
                tableUtilization,
                peakHours,
                lowDemandHours,
                turnover,
                zonePerformance
        );
    }    }
    
    
    
    
    
    
  