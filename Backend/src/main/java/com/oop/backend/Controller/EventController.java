package com.oop.backend.Controller;


import com.oop.backend.DTO.EventDto;
import com.oop.backend.MainClasses.Customer;
import com.oop.backend.DTO.EventConfig;
import com.oop.backend.DTO.EventRequest;
import com.oop.backend.DTO.TicketCountRequest;
import com.oop.backend.Entities.*;

import com.oop.backend.Repository.EventConfigRepository;
import com.oop.backend.Repository.EventRepository;
import com.oop.backend.Repository.TicketSellingRepository;
import com.oop.backend.Service.AuthService;
import com.oop.backend.Service.EventConfigarationService;
import com.oop.backend.MainClasses.TicketPool;
import com.oop.backend.MainClasses.Vendor;
import com.oop.backend.Service.EventService;
import jakarta.servlet.http.HttpServletRequest;

import jakarta.transaction.Transactional;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/events")
@Data
public class EventController {

    @Autowired
    private AuthService authService;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private EventConfigarationService eventConfigarationService;


    @Autowired
    private TicketSellingRepository ticketSellingRepository;

     @Autowired
    private EventService eventService;

    @Autowired
    private EventConfigRepository eventConfigRepository;

    // HashMap to store ticket pools for each event
     private HashMap<Long, TicketPool> ticketPoolMap = new HashMap<>();




    @PostMapping("/create")
    public ResponseEntity<?> createEvent(@RequestBody EventRequest eventRequest, HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token is missing or invalid format");
        }

        VendorEntity vendor = authService.validateVendorToken(token);
        if (vendor == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or expired token");
        }

        Event event = new Event();
        event.setVendor(vendor);
        event.setEventName(eventRequest.getEventName());
        event.setEventDescription(eventRequest.getEventDescription());
        event.setEventDate(eventRequest.getEventDate());
        event.setEventTime(eventRequest.getEventTime());
        event.setEventLocation(eventRequest.getEventLocation());
        event.setTicketPrice(eventRequest.getTicketPrice());
        event.setEventBanner(eventRequest.getEventBanner());

        try {
            Event savedEvent = eventRepository.save(event);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedEvent);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to save event: " + e.getMessage());
        }
    }

    @GetMapping("/mine/graph")
    public List<EventDto> getVendorEventsWithTicketCount(HttpServletRequest request) {
        // Assuming the token is validated and the vendorId is correct
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }

        VendorEntity vendor = authService.validateVendorToken(token);


        return eventService.getVendorEventsWithTicketCount(vendor.getId());
    }

   @DeleteMapping("/{id}")
   @Transactional
    public ResponseEntity<?> deleteEvent(@PathVariable Long id, HttpServletRequest request) {
    String token = request.getHeader("Authorization");
    if (token != null && token.startsWith("Bearer ")) {
        token = token.substring(7);
    } else {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token is missing or invalid format");
    }

    VendorEntity vendor = authService.validateVendorToken(token);
    if (vendor == null) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or expired token");
    }

    Optional<Event> eventOptional = eventRepository.findById(id);
    if (eventOptional.isEmpty()) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Event not found");
    }

    Event event = eventOptional.get();
    if (!event.getVendor().getId().equals(vendor.getId())) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You are not authorized to delete this event");
    }

    // Delete related EventConfigEntities first
    eventConfigRepository.deleteEventConfigByEventId(event.getId());

    // Delete related TicketSelling entities
    ticketSellingRepository.deleteTicketsByEventId(event.getId());

    // Finally, delete the event
    eventRepository.deleteEventById(event.getId());

    return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Event deleted successfully");
}




    @GetMapping("/mine")
    public ResponseEntity<?> getVendorEvents(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token is missing or invalid format");
        }

        VendorEntity vendor = authService.validateVendorToken(token);
        if (vendor == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or expired token");
        }

        List<Event> events = eventRepository.findByVendorId(vendor.getId());
        return ResponseEntity.ok(events);
    }

    @GetMapping("/eventStatus")
    public ResponseEntity<?> getEventStatus(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token is missing or invalid format");
        }

        VendorEntity vendor = authService.validateVendorToken(token);
        if (vendor == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or expired token");
        }

        List<Event> events = eventRepository.findByVendorId(vendor.getId());
        return ResponseEntity.ok(events);
    }

    @GetMapping("/checkout/{eventId}")
    public ResponseEntity<?> displayCheckoutPage(@PathVariable Long eventId, HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7); // Remove "Bearer " from the start of the token
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token is missing or invalid format");
        }

        // Validate the token and extract the vendor details
        CustomerEntity customerEntity = authService.validateCustomerToken(token);
        if (customerEntity == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or expired token");
        }

        // Fetch the event details from the repository
        Event event = eventRepository.findById(eventId).orElse(null);
        System.out.println(event);
        if (event == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Event not found");
        }

        return ResponseEntity.ok(event);
    }


    @GetMapping("/all")
    public ResponseEntity<?> getAllEvents() {
        List<Event> events = eventRepository.findAll();
        return ResponseEntity.ok(events);
    }

    @PostMapping("/start/{eventId}")
    public ResponseEntity<String> startEvent(@PathVariable Long eventId, HttpServletRequest request) {
     String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token is missing or invalid format");
        }

        VendorEntity vendor = authService.validateVendorToken(token);
        if (vendor == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or expired token");
        }

    // Fetch the event details from the repository
    Event event = eventRepository.findById(eventId).orElse(null);
    if (event == null) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Event not found");
    }

    // Check if the authenticated vendor is the creator of the event
    if (!event.getVendor().equals(vendor)) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Only the event creator can start the event");
    }

    // Check if event is already active
    if (event.isEventActive()) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body("Event already started");
    }

    // Fetch the ticket pool configuration details
    EventConfigEntity eventConfig = eventConfigRepository.findByEventId(eventId);
    if (eventConfig == null) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Ticket pool configuration not found for event ID " + eventId);
    }

    // Create a ticket pool for this event and store it in the HashMap
    TicketPool ticketPool = new TicketPool(eventConfig.getMaxCapacity());
    ticketPoolMap.put(eventId, ticketPool);

    // Create a thread for the vendor to manage ticket pool
    Vendor vendorThread = new Vendor(eventConfig.getTotalTickets(), eventConfig.getReleaseRate(), ticketPool, eventId, event.getEventName(), vendor.getId());
    Thread thread = new Thread(vendorThread , vendor.getId());
    thread.start();

    event.setEventActive(true);
    eventRepository.save(event);

    return ResponseEntity.ok("Event started and ticket pool created for event ID " + eventId);
}
    @PostMapping("/buy/{eventId}")
    public ResponseEntity<String> buyTicket(@PathVariable Long eventId, @RequestBody TicketCountRequest ticketCountRequest, HttpServletRequest request) {
    // Retrieve the token from the request header
    String token = request.getHeader("Authorization");
    if (token != null && token.startsWith("Bearer ")) {
        token = token.substring(7); // Remove "Bearer " from the start of the token
    } else {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token is missing or invalid format");
    }

    // Validate the token and extract the customer details
    CustomerEntity customer = authService.validateCustomerToken(token);
    if (customer == null) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or expired token");
    }

    // Fetch the event details from the repository
    Event event = eventRepository.findById(eventId).orElse(null);
    if (event == null) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Event not found");
    }

    // Check if the event is active
    if (!event.isEventActive()) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body("Event is not active");
    }

    //find the ticket pool configuration details
    EventConfigEntity eventConfig = eventConfigRepository.findByEventId(eventId);
    int customerRetrivelRate = eventConfig.getRetrievalRate();


    // Retrieve the ticket pool for the event
    TicketPool ticketPool = ticketPoolMap.get(eventId);
    if (ticketPool == null) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Ticket pool not found for event ID " + eventId);
    }

    // Create a thread for the customer to buy tickets
    Customer customerThread = new Customer(ticketPool, customerRetrivelRate,ticketCountRequest.getTicketCount());
    Thread thread = new Thread(customerThread,customer.getCustomerId());
    thread.start();

    // Save the ticket purchase details in the repository
    TicketSelling ticketSelling = new TicketSelling();
    ticketSelling.setEvent(event);
    ticketSelling.setCustomerEntity(customer);
    ticketSelling.setQuantity(ticketCountRequest.getTicketCount());
    ticketSelling.setPurchaseDate(LocalDateTime.now());
    ticketSellingRepository.save(ticketSelling);

    return ResponseEntity.ok("Ticket purchase process started for event ID " + eventId);
}


    @GetMapping("/bookinghistory")
    public ResponseEntity<?> getBookingHistory(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token is missing or invalid format");
        }

        CustomerEntity customer = authService.validateCustomerToken(token);
        if (customer == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or expired token");
        }

        List<TicketSelling> ticketSellings = ticketSellingRepository.findByCustomerEntityCustomerId(customer.getCustomerId());
        return ResponseEntity.ok(ticketSellings);
    }






    @PostMapping("/configureticketpool/{eventId}")
    public ResponseEntity<String> addTicket(@PathVariable Long eventId, @RequestBody EventConfig ticketPoolConfigDetails, HttpServletRequest request) {


        // Retrieve the token from the request header
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7); // Remove "Bearer " from the start of the token
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token is missing or invalid format");
        }

        // Validate the token and extract the vendor details
        VendorEntity vendor = authService.validateVendorToken(token);
        if (vendor == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or expired token");
        }

        // Fetch the event details from the repository
        Event event = eventRepository.findById(eventId).orElse(null);
        if (event == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Event not found");
        }

        // Check if the authenticated vendor is the creator of the event
        if (!event.getVendor().equals(vendor)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Only the event creator can start the event");
        }

        //Check event is active or not
        if (event.isEventActive()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Event already started");
        }

        eventConfigarationService.saveOrUpdateEventConfig(eventId, ticketPoolConfigDetails);


    return ResponseEntity.ok("Ticket Pool is being added for Event ID: " + eventId);
    }


    @GetMapping("/configureticketpool/{eventId}")
    public ResponseEntity<?> getTicketPoolConfigDetails(@PathVariable Long eventId, HttpServletRequest request) {
        // Retrieve the token from the request header
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7); // Remove "Bearer " from the start of the token
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token is missing or invalid format");
        }

        // Validate the token and extract the vendor details
        VendorEntity vendor = authService.validateVendorToken(token);
        if (vendor == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or expired token");
        }

        // Fetch the event details from the repository
        Event event = eventRepository.findById(eventId).orElse(null);
        if (event == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Event not found");
        }

        // Check if the authenticated vendor is the creator of the event
        if (!event.getVendor().equals(vendor)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Only the event creator can start the event");
        }

        // Fetch the ticket pool configuration details
        EventConfigEntity eventConfig = eventConfigRepository.findByEventId(eventId);
        if (eventConfig == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Ticket pool configuration not found for event ID " + eventId);
        }

        return ResponseEntity.ok(eventConfig);
    }

    @PostMapping("/stop/{eventId}")
public ResponseEntity<String> stopEvent(@PathVariable Long eventId, HttpServletRequest request) {
    // Retrieve the token from the request header
    String token = request.getHeader("Authorization");
    if (token != null && token.startsWith("Bearer ")) {
        token = token.substring(7); // Remove "Bearer " from the start of the token
    } else {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token is missing or invalid format");
    }

    // Validate the token and extract the vendor details
    VendorEntity vendor = authService.validateVendorToken(token);
    if (vendor == null) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or expired token");
    }

    // Fetch the event details from the repository
    Event event = eventRepository.findById(eventId).orElse(null);
    if (event == null) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Event not found");
    }

    // Check if the authenticated vendor is the creator of the event
    if (!event.getVendor().equals(vendor)) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Only the event creator can stop the event");
    }

    // Check if the event is active
    if (!event.isEventActive()) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body("Event is not active");
    }

    // Mark the event as inactive
    event.setEventActive(false);
    eventRepository.save(event);

    // Remove the ticket pool associated with this event
    ticketPoolMap.remove(eventId);

    return ResponseEntity.ok("Event has been successfully stopped for event ID " + eventId);
}


//view booked details (As vendor)
    @GetMapping("/viewbookeddetails/{eventId}")
    public ResponseEntity<?> viewBookedDetails(@PathVariable Long eventId, HttpServletRequest request) {
        // Retrieve the token from the request header
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7); // Remove "Bearer " from the start of the token
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token is missing or invalid format");
        }

        // Validate the token and extract the vendor details
        VendorEntity vendor = authService.validateVendorToken(token);
        if (vendor == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or expired token");
        }

        // Fetch the event details from the repository
        Event event = eventRepository.findById(eventId).orElse(null);
        if (event == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Event not found");
        }

        // Check if the authenticated vendor is the creator of the event
        if (!event.getVendor().equals(vendor)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Only the event creator can view the booked details");
        }

        // Fetch the ticket selling details for this event
        List<TicketSelling> ticketSellings = ticketSellingRepository.findByEventId(eventId);
        return ResponseEntity.ok(ticketSellings);
    }




}