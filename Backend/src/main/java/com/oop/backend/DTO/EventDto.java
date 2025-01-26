package com.oop.backend.DTO;

import lombok.Data;

@Data
public class EventDto {
    private String eventName;
    private int soldTickets;
    private int totalTickets;
}
