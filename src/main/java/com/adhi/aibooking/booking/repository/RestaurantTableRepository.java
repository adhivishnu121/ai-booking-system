package com.adhi.aibooking.booking.repository;

import com.adhi.aibooking.booking.entity.RestaurantTable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RestaurantTableRepository extends JpaRepository<RestaurantTable, String> {

    List<RestaurantTable> findByRestaurantId(String restaurantId);
}