package com.adhi.aibooking.booking.repository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import com.adhi.aibooking.booking.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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
	@Query("""
			SELECT SUM(r.partySize) * 1.0 / SUM(t.capacity)
			FROM Reservation r
			JOIN r.table t
			""")
			Double getTableUtilization();
	@Query("""
			SELECT EXTRACT(HOUR FROM r.bookingTime), COUNT(r)
			FROM Reservation r
			GROUP BY EXTRACT(HOUR FROM r.bookingTime)
			HAVING COUNT(r) < 2
			""")
			List<Object[]> getLowDemandHours();
	@Query("""
					SELECT t.zone, COUNT(r)
					FROM Reservation r
					JOIN r.table t
					GROUP BY t.zone
					""")
					List<Object[]> getZonePerformance();
					@Query("""
							SELECT t.id, COUNT(r)
							FROM Reservation r
							JOIN r.table t
							GROUP BY t.id
							""")
							List<Object[]> getTableTurnover();
					@Query(value = """
						    SELECT EXTRACT(HOUR FROM booking_time) AS hour, COUNT(*)
						    FROM reservations
						    GROUP BY EXTRACT(HOUR FROM booking_time)
						    ORDER BY COUNT(*) DESC
						""", nativeQuery = true)
						List<Object[]> getPeakHours();

						
					@Query("""
						SELECT COUNT(r)
						FROM Reservation r
						WHERE r.bookingTime >= :start
						AND r.bookingTime < :end
						""")
						long getTotalBookingsByDateRange(@Param("start") LocalDateTime start,
						                                 @Param("end") LocalDateTime end);

					@Query("""
							SELECT COALESCE(SUM(r.partySize), 0)
							FROM Reservation r
							WHERE r.bookingTime >= :start
							AND r.bookingTime < :end
							""")
							long getTotalCoversByDateRange(@Param("start") LocalDateTime start,
							                               @Param("end") LocalDateTime end); 
					@Query("""
							SELECT COALESCE(AVG(r.partySize), 0)
							FROM Reservation r
							WHERE r.bookingTime >= :start
							AND r.bookingTime < :end
							""")
							double getAveragePartySizeByDateRange(@Param("start") LocalDateTime start,
							                                     @Param("end") LocalDateTime end);

					@Query("""
						SELECT EXTRACT(HOUR FROM r.bookingTime), COUNT(r)
						FROM Reservation r
						WHERE r.bookingTime >= :start
						AND r.bookingTime < :end
						GROUP BY EXTRACT(HOUR FROM r.bookingTime)
						ORDER BY COUNT(r) DESC
						""")
						List<Object[]> getPeakHoursByDateRange(@Param("start") LocalDateTime start,
						                                      @Param("end") LocalDateTime end);

						@Query("""
						SELECT EXTRACT(HOUR FROM r.bookingTime), COUNT(r)
						FROM Reservation r
						WHERE r.bookingTime >= :start
						AND r.bookingTime < :end
						GROUP BY EXTRACT(HOUR FROM r.bookingTime)
						HAVING COUNT(r) < 2
						""")
						List<Object[]> getLowDemandHoursByDateRange(@Param("start") LocalDateTime start,
						                                           @Param("end") LocalDateTime end);

						@Query("""
						SELECT t.id, COUNT(r)
						FROM Reservation r
						JOIN r.table t
						WHERE r.bookingTime >= :start
						AND r.bookingTime < :end
						GROUP BY t.id
						""")
						List<Object[]> getTableTurnoverByDateRange(@Param("start") LocalDateTime start,
						                                          @Param("end") LocalDateTime end);

						@Query("""
						SELECT t.zone, COUNT(r)
						FROM Reservation r
						JOIN r.table t
						WHERE r.bookingTime >= :start
						AND r.bookingTime < :end
						GROUP BY t.zone
						""")
						List<Object[]> getZonePerformanceByDateRange(@Param("start") LocalDateTime start,
						                                            @Param("end") LocalDateTime end);
						@Query("""
								SELECT COALESCE(SUM(r.partySize) * 1.0 / SUM(t.capacity), 0)
								FROM Reservation r
								JOIN r.table t
								WHERE r.bookingTime >= :start
								AND r.bookingTime < :end
								""")
								double getTableUtilizationByDateRange(@Param("start") LocalDateTime start,
								                                     @Param("end") LocalDateTime end);
						List<Reservation> findByBookingTimeBetween(LocalDateTime start, LocalDateTime end);
								
								
								}