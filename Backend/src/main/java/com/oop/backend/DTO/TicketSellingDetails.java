package com.oop.backend.DTO;

import com.oop.backend.Entities.CustomerEntity;

import java.time.LocalDateTime;

public class TicketSellingDetails {
    private LocalDateTime purchaseDate;
    private int quantity;
    private CustomerEntity customerEntity;  // Use CustomerEntity instead of customerId
    private Long eventId;

    public TicketSellingDetails(LocalDateTime purchaseDate, int quantity, CustomerEntity customerEntity, Long eventId) {
        this.purchaseDate = purchaseDate;
        this.quantity = quantity;
        this.customerEntity = customerEntity;
        this.eventId = eventId;
    }

    // Getters and setters
}

