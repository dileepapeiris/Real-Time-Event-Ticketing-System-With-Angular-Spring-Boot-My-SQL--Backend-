package com.oop.backend.MainClasses;

public class Customer implements Runnable {
    private TicketPool ticketPool;
    private int customerRetrievalRate;
    private int ticketCount;

    public Customer(TicketPool ticketPool, int customerRetrievalRate, int ticketCount) {
        this.ticketPool = ticketPool;
        this.customerRetrievalRate = customerRetrievalRate;
        this.ticketCount = ticketCount;
    }

    @Override
    public void run() {
        for (int i = 0; i < ticketCount; i++) {
            Ticket ticket = ticketPool.buyTicket(); // Thread-safe retrieval
            try {
                Thread.sleep(customerRetrievalRate * 1000); // Retrieval delay
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}

