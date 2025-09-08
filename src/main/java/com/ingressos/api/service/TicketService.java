package com.ingressos.api.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ingressos.api.dto.GeneralDto;
import com.ingressos.api.enums.TicketStatus;
import com.ingressos.api.exceptions.IncorrectParameterException;
import com.ingressos.api.exceptions.IsNullException;
import com.ingressos.api.model.EventModel;
import com.ingressos.api.model.OrderItemModel;
import com.ingressos.api.model.TicketLotModel;
import com.ingressos.api.model.TicketModel;
import com.ingressos.api.model.UserModel;
import com.ingressos.api.repository.TicketLotRepository;
import com.ingressos.api.repository.TicketRepository;
import com.ingressos.api.util.InternationalizationUtil;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class TicketService {

    @Autowired
    private TicketLotRepository repository;
    @Autowired
    private TicketRepository ticketRepository;
    @Autowired
    private QRCodeService qrCodeService;
    @Autowired
    private InternationalizationUtil messageIniernat;

    @Transactional(readOnly = true)
    public List<TicketLotModel> findAll() {
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public TicketLotModel findById(GeneralDto dto) {
        if (dto == null || dto.id() == null) {
            throw new IsNullException(messageIniernat.getMessage("id.null"));
        }
        return repository.findById(dto.id())
                .orElseThrow(() -> new IncorrectParameterException(
                messageIniernat.getMessage("descriptions.ticketlot.notfound")));
    }

    // Métodos para gerenciamento de ingressos individuais
    @Transactional
    public TicketModel createTicket(OrderItemModel orderItem, UserModel user, EventModel event) {
        TicketModel ticket = new TicketModel();
        ticket.setOrderItem(orderItem);
        ticket.setUser(user);
        ticket.setEvent(event);
        ticket.setStatus(TicketStatus.AVAILABLE);
        ticket.setCreatedAt(LocalDateTime.now());
        ticket.setUpdatedAt(LocalDateTime.now());

        // Gera hash único para o ingresso
        ticket.setQrCodeHash(UUID.randomUUID().toString());

        // Salva o ticket primeiro para obter o ID
        ticket = ticketRepository.save(ticket);

        // Gera QR Code com o ID do ticket
        String qrCodeData = qrCodeService.generateQRCodeData(
                ticket.getId(),
                event.getId(),
                user.getEmail()
        );
        String qrCodeImage = qrCodeService.generateTicketQRCode(
                ticket.getId(),
                event.getId(),
                user.getEmail()
        );

        ticket.setQrCodeData(qrCodeData);
        ticket.setQrCodeImage(qrCodeImage);
        ticket.setUpdatedAt(LocalDateTime.now());

        return ticketRepository.save(ticket);
    }

    public Optional<TicketModel> findTicketById(Integer id) {
        return ticketRepository.findById(id);
    }

    public List<TicketModel> findByUser(UserModel user) {
        return ticketRepository.findByUser(user);
    }

    public List<TicketModel> findByEvent(EventModel event) {
        return ticketRepository.findByEvent(event);
    }

    @Transactional
    public TicketModel useTicket(Integer ticketId) {
        Optional<TicketModel> optionalTicket = ticketRepository.findById(ticketId);
        if (optionalTicket.isEmpty()) {
            throw new IncorrectParameterException("Ingresso não encontrado");
        }

        TicketModel ticket = optionalTicket.get();
        if (ticket.getStatus() != TicketStatus.SOLD) {
            throw new IncorrectParameterException("Ingresso não está disponível para uso");
        }

        ticket.setStatus(TicketStatus.USED);
        ticket.setUsedAt(LocalDateTime.now());
        ticket.setUpdatedAt(LocalDateTime.now());

        return ticketRepository.save(ticket);
    }

    @Transactional
    public TicketModel markAsSold(Integer ticketId) {
        Optional<TicketModel> optionalTicket = ticketRepository.findById(ticketId);
        if (optionalTicket.isEmpty()) {
            throw new IncorrectParameterException("Ingresso não encontrado");
        }

        TicketModel ticket = optionalTicket.get();
        if (ticket.getStatus() != TicketStatus.RESERVED && ticket.getStatus() != TicketStatus.AVAILABLE) {
            throw new IncorrectParameterException("Ingresso não está disponível para venda");
        }

        ticket.setStatus(TicketStatus.SOLD);
        ticket.setUpdatedAt(LocalDateTime.now());

        return ticketRepository.save(ticket);
    }

    public boolean validateTicketQRCode(String qrCodeData) {
        if (!qrCodeService.validateQRCode(qrCodeData)) {
            return false;
        }

        try {
            Integer ticketId = qrCodeService.extractTicketId(qrCodeData);
            Optional<TicketModel> optionalTicket = ticketRepository.findById(ticketId);

            if (optionalTicket.isEmpty()) {
                return false;
            }

            TicketModel ticket = optionalTicket.get();
            return ticket.getQrCodeData().equals(qrCodeData)
                    && ticket.getStatus() == TicketStatus.SOLD;
        } catch (Exception e) {
            return false;
        }
    }
}
