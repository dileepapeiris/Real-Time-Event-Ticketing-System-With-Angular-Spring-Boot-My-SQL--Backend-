package com.oop.backend.MainClasses;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.locks.ReentrantLock;

import java.util.concurrent.ConcurrentLinkedQueue;


import java.util.concurrent.locks.Condition;


public class TicketPool {
    private static final Logger logger = LoggerFactory.getLogger(TicketPool.class);
    private final int maximumTicketCapacity;
    private final ConcurrentLinkedQueue<Ticket> ticketQueue; // Thread-safe queue
    private final ReentrantLock lock; // ReentrantLock for synchronization
    private final Condition notFull; // Condition for producers
    private final Condition notEmpty; // Condition for consumers


    public TicketPool(int maximumTicketCapacity) {
        this.maximumTicketCapacity = maximumTicketCapacity;
        this.ticketQueue = new ConcurrentLinkedQueue<>();
        this.lock = new ReentrantLock();
        this.notFull = lock.newCondition();
        this.notEmpty = lock.newCondition();

    }

    // Add a ticket to the pool
    public  void addTicket(Ticket ticket) {
        lock.lock();
        try {
            while (ticketQueue.size() >= maximumTicketCapacity) {
                System.out.println("TicketPool is full. Vendor is waiting to add tickets...");
                logger.info("TicketPool is full. Vendor is waiting to add tickets...");
                notFull.await(); // Wait until there is space in the queue
            }

            ticketQueue.add(ticket);
            System.out.println("Ticket added by Vendor with Id: " + Thread.currentThread().getName() +
                    " - Current queue size: " + ticketQueue.size());
            logger.info("Ticket added by Vendor with Id : {}- Current queue size: {} - Event Name: {}",
                        Thread.currentThread().getName(), ticketQueue.size(), ticket.getEventName());
            notEmpty.signalAll(); // Notify waiting consumers
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // Restore interrupt status
            logger.info("Ticket pool Stopped selling tickets {}",ticket.getEventName());
            throw new RuntimeException("Thread interrupted while adding ticket: " + e.getMessage());
        } finally {
            lock.unlock();
        }
    }

    // Retrieve tickets from the pool
    public Ticket buyTicket() {
        lock.lock();
        try {
            while (ticketQueue.isEmpty()) {
                System.out.println("TicketPool is empty. Customer is waiting to buy tickets...");
                logger.info("TicketPool is empty. Customer is waiting to buy tickets...");
                notEmpty.await(); // Wait until there are tickets available
            }

            Ticket ticket = null;
            if (!ticketQueue.isEmpty()) {
                ticket = ticketQueue.poll();
                System.out.println("Ticket bought by customer with Id : " + Thread.currentThread().getName() +
                        " - Current queue size: " + ticketQueue.size());
                logger.info("Ticket bought by customer with Id  {} - Current queue size: {} - Event Name: {}",
                        Thread.currentThread().getName(), ticketQueue.size(), ticket.getEventName());
            }

            notFull.signalAll(); // Notify waiting producers
            return ticket;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // Restore interrupt status
            throw new RuntimeException("Thread interrupted while buying ticket: " + e.getMessage());
        } finally {
            lock.unlock();
        }
    }

    public int getTicketQueueSize() {

        return ticketQueue.size();
    }

}

