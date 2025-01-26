package com.oop.backend.MainClasses;



import lombok.Data;

@Data
public class Ticket {
    private int ticketId;
    private String eventName;
    private Long eventId;// Add eventId to the ticket
    private String vendorId ;


    public Ticket(int ticketId, String eventName, Long eventId, String vendorId) {
        this.ticketId = ticketId;
        this.eventName = eventName;
        this.eventId = eventId;
        this.vendorId = vendorId;
    }



    @Override
    public String toString() {
        return "Ticket{" +
                "ticketId=" + ticketId +
                ", eventName='" + eventName + '\'' +
                ", eventId='" + eventId + '\'' +
                '}';
    }
}
