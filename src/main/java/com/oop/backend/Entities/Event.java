package com.oop.backend.Entities;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

import jakarta.persistence.*;
import lombok.Getter;

import java.util.List;

@Entity
@Data
@Getter
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "vendor_id", nullable = false) // Foreign key column
    @JsonIgnore // Prevent serialization of the vendor field to avoid deep nesting
    private VendorEntity vendor; // Reference to the VendorEntity

    private String eventName;     // Name of the event
    @Column(length = 500)
    private String eventDescription;   // Description of the event
    private String eventDate;     // Date of the event (e.g., 2024-12-31)
    private String eventTime;     // Time of the event (e.g., 15:00)
    private String eventLocation;
    private String ticketPrice;   // Frontend passes price as a string


    @Column(columnDefinition = "TEXT")
    private String eventBanner;     // URL for the event banner image

    @Column(nullable = false) // Ensure column is not null
     @JsonProperty("isEventActive") // Explicitly map to `EventActive` for the front-end
    private boolean isEventActive = false; // Default value set to false



}



