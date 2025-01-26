package com.oop.backend.Repository;

import com.oop.backend.Entities.Event;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> findByVendorId(String vendorId);


    @Modifying
    @Query("DELETE FROM Event e WHERE e.id = :eventId")
    void deleteEventById(Long eventId);





}
