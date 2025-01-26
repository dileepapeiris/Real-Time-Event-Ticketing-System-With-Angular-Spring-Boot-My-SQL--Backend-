package com.oop.backend.DTO;


import lombok.*;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventConfig {
    private int maxCapacity;
    private int totalTickets;
    private int retrievalRate;
    private int releaseRate;

}
