package com.oop.backend.Entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EventConfigEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long eventConfigId;


    @JsonProperty("maxCapacity")
    private int maxCapacity;

    @JsonProperty("totalTickets")
    private int totalTickets;

    @JsonProperty("retrievalRate")
    private int retrievalRate;
    @JsonProperty("releaseRate")
    private int releaseRate;

    // One-to-One relationship with Event
    @ManyToOne
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;

}
