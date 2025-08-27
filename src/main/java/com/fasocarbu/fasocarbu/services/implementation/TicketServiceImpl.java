package com.fasocarbu.fasocarbu.services.implementation;

import com.fasocarbu.fasocarbu.models.Ticket;
import com.fasocarbu.fasocarbu.repositories.TicketRepository;
import com.fasocarbu.fasocarbu.services.interfaces.TicketService;
import com.fasocarbu.fasocarbu.utils.QRCodeGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class TicketServiceImpl implements TicketService {

    @Autowired
    private TicketRepository ticketRepository;
    @Autowired
    private QRCodeGenerator qrCodeGenerator;

    @Override
    public Ticket enregistrerTicket(Ticket ticket) {
        try {
            String codeQr = qrCodeGenerator.generateQRCodeForTicket(ticket);
            ticket.setCodeQr(codeQr);
        } catch (Exception e) {

            throw new RuntimeException("Erreur lors de la génération du code QR", e);
        }
        return ticketRepository.save(ticket);
    }

    @Override
    public Ticket getTicketById(Long id) {
        Optional<Ticket> optional = ticketRepository.findById(id);
        return optional.orElse(null);
    }

    @Override
    public List<Ticket> getAllTickets() {
        return ticketRepository.findAll();
    }

    @Override
    public void supprimerTicket(Long id) {
        ticketRepository.deleteById(id);
    }

    @Override
    public List<Ticket> getTicketsByUtilisateur(UUID utilisateurId) {
        return ticketRepository.findByUtilisateur_Id(utilisateurId);
    }

}
