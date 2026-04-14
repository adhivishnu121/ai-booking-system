package com.adhi.aibooking.booking.repository;
import java.time.LocalDateTime;
import java.util.List;
import com.adhi.aibooking.booking.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, String> {
	List<Reservation> findByTableIdAndBookingTime(String tableId, LocalDateTime bookingTime);
	@Query("SELECT COUNT(r) FROM Reservation r")
	long getTotalBookings();
	
	@Query("SELECT SUM(r.partySize) FROM Reservation r")
	long getTotalCovers();
	
	@Query("SELECT AVG(r.partySize) FROM Reservation r")
	Double getAveragePartySize();

	@Query(value = """
		    SELECT EXTRACT(HOUR FROM booking_time) AS hour, COUNT(*)
		    FROM reservations
		    GROUP BY EXTRACT(HOUR FROM booking_time)
		    ORDER BY COUNT(*) DESC
		""", nativeQuery = true)
		List<Object[]> getPeakHours();}