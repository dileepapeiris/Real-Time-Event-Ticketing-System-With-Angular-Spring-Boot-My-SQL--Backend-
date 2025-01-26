package com.oop.backend.Service;




import com.oop.backend.DTO.EventConfig;
import com.oop.backend.Entities.EventConfigEntity;
import com.oop.backend.Repository.EventConfigRepository;
import com.oop.backend.Repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class EventConfigarationService {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private EventConfigRepository eventConfigRepository;

    // Save or update EventConfigEntity
    public EventConfigEntity saveOrUpdateEventConfig(Long eventId, EventConfig eventConfig) {
        EventConfigEntity eventConfigDetails = eventConfigRepository.findByEventId(eventId);
        if (eventConfigDetails == null) {
            eventConfigDetails = new EventConfigEntity();
            eventConfigDetails.setEvent(eventRepository.findById(eventId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Event not found")));
            eventConfigDetails.setTotalTickets(eventConfig.getTotalTickets());
            eventConfigDetails.setMaxCapacity(eventConfig.getMaxCapacity());
            eventConfigDetails.setRetrievalRate(eventConfig.getRetrievalRate());
            eventConfigDetails.setReleaseRate(eventConfig.getReleaseRate());
            eventConfigRepository.save(eventConfigDetails);



        } else {
            eventConfigDetails.setTotalTickets(eventConfig.getTotalTickets());
            eventConfigDetails.setMaxCapacity(eventConfig.getMaxCapacity());
            eventConfigDetails.setRetrievalRate(eventConfig.getRetrievalRate());
            eventConfigDetails.setReleaseRate(eventConfig.getReleaseRate());
            eventConfigRepository.save(eventConfigDetails);

        }
        return eventConfigDetails;
    }


}
