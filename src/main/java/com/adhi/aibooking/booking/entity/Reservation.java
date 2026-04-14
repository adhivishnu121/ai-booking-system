package com.adhi.aibooking.booking.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "reservations")
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private int partySize;

    private LocalDateTime bookingTime;

    @Column(length = 1000)
    private String specialNotes;

    private String status;

    @ManyToOne
    @JoinColumn(name = "table_id")
    @JsonIgnoreProperties({"reservations", "restaurant"})

    private RestaurantTable table;
}