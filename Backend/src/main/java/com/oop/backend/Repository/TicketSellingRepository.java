package com.oop.backend.Repository;

import com.oop.backend.Entities.TicketSelling;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;



public interface TicketSellingRepository extends JpaRepository<TicketSelling, Long> {  // Use Long for the ID type


    List<TicketSelling> findByCustomerEntityCustomerId(String customerId);

    List<TicketSelling> findByEventId(Long eventId);


    @Query("SELECT COALESCE(SUM(t.quantity), 0) FROM TicketSelling t WHERE t.event.id = :eventId")
    Integer sumQuantityByEventId(Long eventId);



    @Transactional
    @Modifying
    @Query("DELETE FROM TicketSelling t WHERE t.event.id = :eventId")
    void deleteTicketsByEventId(Long eventId);
}

