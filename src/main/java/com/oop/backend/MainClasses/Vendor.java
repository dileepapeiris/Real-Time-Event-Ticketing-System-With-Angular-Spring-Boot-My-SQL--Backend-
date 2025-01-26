package com.oop.backend.MainClasses;

public class Vendor implements Runnable {
    private int totalTickets; // Tickets willing to sell
    private int ticketReleaseRate; // Frequency of releasing tickets
    private TicketPool ticketPool; // Shared resource between Vendors and Customers
    private Long eventId; // Current session event ID
    private String eventName; // Event name for tickets
    private String vendorId;
    private volatile boolean running = true;

    public Vendor(int totalTickets, int ticketReleaseRate, TicketPool ticketPool, Long eventId, String eventName, String vendorId) {
        this.totalTickets = totalTickets;
        this.ticketReleaseRate = ticketReleaseRate;
        this.ticketPool = ticketPool;
        this.eventId = eventId;
        this.eventName = eventName;
        this.vendorId = vendorId;
    }

    @Override
    public void run() {
        while (running) {
            for (int i = 1; i <= totalTickets; i++) {
                // Create a ticket using the event ID and event name
                Ticket ticket = new Ticket(i, eventName, eventId, vendorId);
                // Add ticket to the pool
                ticketPool.addTicket(ticket);
                try {
                    Thread.sleep(ticketReleaseRate * 1000); // Release rate in milliseconds
                } catch (InterruptedException e) {
                    // If the thread is interrupted, it should stop
                    Thread.currentThread().interrupt();
                    break; // Exit the loop safely
                }
            }
        }
    }

    public void stopThread() {
        this.running = false; // Set running to false to stop the thread
    }
}
