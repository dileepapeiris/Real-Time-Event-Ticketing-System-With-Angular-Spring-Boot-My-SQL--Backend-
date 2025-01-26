package com.oop.backend.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TicketSelling {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long purchaseId;  // ID type should be Long

    @ManyToOne
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;

    @ManyToOne
    @JoinColumn(name = "customerId", nullable = false)  // Foreign key for CustomerEntity
    private CustomerEntity customerEntity;  // Customer who purchased the ticket

    private LocalDateTime purchaseDate;  // Date of purchase
    private int quantity;  // Quantity of tickets purchased
}

