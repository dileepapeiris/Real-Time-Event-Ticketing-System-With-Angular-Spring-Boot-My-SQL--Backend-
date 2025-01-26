package com.oop.backend.Service;


import com.oop.backend.DTO.EventDto;
import com.oop.backend.Entities.Event;
import com.oop.backend.Entities.EventConfigEntity;
import com.oop.backend.Repository.EventConfigRepository;
import com.oop.backend.Repository.EventRepository;
import com.oop.backend.Repository.TicketSellingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EventService {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private TicketSellingRepository ticketSellingRepository;

    @Autowired
    private EventConfigRepository eventConfigRepository;  // Assuming EventConfigEntity has max capacity

    // Fetch events and calculate sold tickets
    public List<EventDto> getVendorEventsWithTicketCount(String vendorId) {
        List<Event> events = eventRepository.findByVendorId(vendorId);
        return events.stream().map(event -> {
            EventDto eventDTO = new EventDto();
            eventDTO.setEventName(event.getEventName());

            // Get the total tickets from EventConfig
            EventConfigEntity eventConfig = eventConfigRepository.findByEventId(event.getId());
            if (eventConfig != null) {
                eventDTO.setTotalTickets(eventConfig.getTotalTickets());
            } else {
                eventDTO.setTotalTickets(0); // No config found
            }

            // Calculate sold tickets for this event
            int soldTickets = ticketSellingRepository.sumQuantityByEventId(event.getId());
            eventDTO.setSoldTickets(soldTickets);

            return eventDTO;
        }).collect(Collectors.toList());
    }
}