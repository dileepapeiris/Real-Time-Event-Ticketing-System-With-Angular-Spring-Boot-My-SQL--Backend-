package com.oop.backend.Repository;

import com.oop.backend.Entities.EventConfigEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface EventConfigRepository extends JpaRepository<EventConfigEntity, Long> {
    EventConfigEntity findByEventId(Long eventId);

    @Transactional
    @Modifying
    @Query("DELETE FROM EventConfigEntity e WHERE e.event.id = :eventId")
    void deleteEventConfigByEventId(Long eventId);
}
