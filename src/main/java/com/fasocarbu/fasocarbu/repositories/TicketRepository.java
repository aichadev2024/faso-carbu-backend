package com.fasocarbu.fasocarbu.repositories;

import com.fasocarbu.fasocarbu.models.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
}
