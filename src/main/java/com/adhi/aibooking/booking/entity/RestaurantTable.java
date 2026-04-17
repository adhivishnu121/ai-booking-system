package com.adhi.aibooking.booking.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "restaurant_tables")
public class RestaurantTable {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false)
    private int tableNumber;

    @Column(nullable = false)
    private int capacity;

    private String zone;

    private boolean windowSeat = false;

    private boolean vip = false;

    private boolean mergeable = false;

    @Column(nullable = false)
    private String status = "AVAILABLE";

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;
}