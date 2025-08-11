package com.fasocarbu.fasocarbu.repositories;

import com.fasocarbu.fasocarbu.models.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
    Optional<Ticket> findByCodeQr(String codeQr);

}
