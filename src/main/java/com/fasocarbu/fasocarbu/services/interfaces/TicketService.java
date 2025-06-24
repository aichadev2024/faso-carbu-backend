package com.fasocarbu.fasocarbu.services.interfaces;

import com.fasocarbu.fasocarbu.models.Ticket;

import java.util.List;

public interface TicketService {
    Ticket enregistrerTicket(Ticket ticket);
    Ticket getTicketById(Long id);
    List<Ticket> getAllTickets();
    void supprimerTicket(Long id);
}
