package com.oop.backend.DTO;

import lombok.Data;

@Data
public class EventRequest {
    private String eventName;     // Name of the event
    private String eventDescription;   // Description of the event
    private String eventDate;     // Date of the event (e.g., 2024-12-31)
    private String eventTime;     // Time of the event (e.g., 15:00)
    private String eventLocation;
    private String ticketPrice;   // Frontend passes price as a string
    private String eventBanner;     // URL for the event banner image

    // Getters and Setters
}
