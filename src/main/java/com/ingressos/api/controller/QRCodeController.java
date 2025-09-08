package com.ingressos.api.controller;

import com.ingressos.api.model.TicketModel;
import com.ingressos.api.service.QRCodeService;
import com.ingressos.api.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/qrcode")
@CrossOrigin(origins = "*", maxAge = 3600)
public class QRCodeController {

    @Autowired
    private QRCodeService qrCodeService;

    @Autowired
    private TicketService ticketService;

    @PostMapping("/validate")
    @PreAuthorize("hasRole('ADMIN') or hasRole('ORGANIZER')")
    public ResponseEntity<Map<String, Object>> validateQRCode(@RequestBody Map<String, String> request) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            String qrCodeData = request.get("qrCodeData");
            
            if (qrCodeData == null || qrCodeData.trim().isEmpty()) {
                response.put("valid", false);
                response.put("message", "Dados do QR Code não fornecidos");
                return ResponseEntity.badRequest().body(response);
            }

            boolean isValid = ticketService.validateTicketQRCode(qrCodeData);
            response.put("valid", isValid);
            
            if (isValid) {
                Integer ticketId = qrCodeService.extractTicketId(qrCodeData);
                Optional<TicketModel> ticketOpt = ticketService.findTicketById(ticketId);
                
                if (ticketOpt.isPresent()) {
                    TicketModel ticket = ticketOpt.get();
                    response.put("message", "QR Code válido");
                    response.put("ticketId", ticket.getId());
                    response.put("eventId", ticket.getEvent().getId());
                    response.put("eventName", ticket.getEvent().getName());
                    response.put("userName", ticket.getUser().getName());
                    response.put("userEmail", ticket.getUser().getEmail());
                    response.put("status", ticket.getStatus().toString());
                }
            } else {
                response.put("message", "QR Code inválido ou ingresso não encontrado");
            }
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            response.put("valid", false);
            response.put("message", "Erro ao validar QR Code: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PostMapping("/use-ticket")
    @PreAuthorize("hasRole('ADMIN') or hasRole('ORGANIZER')")
    public ResponseEntity<Map<String, Object>> useTicket(@RequestBody Map<String, String> request) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            String qrCodeData = request.get("qrCodeData");
            
            if (qrCodeData == null || qrCodeData.trim().isEmpty()) {
                response.put("success", false);
                response.put("message", "Dados do QR Code não fornecidos");
                return ResponseEntity.badRequest().body(response);
            }

            // Primeiro valida o QR Code
            if (!ticketService.validateTicketQRCode(qrCodeData)) {
                response.put("success", false);
                response.put("message", "QR Code inválido");
                return ResponseEntity.badRequest().body(response);
            }

            // Extrai o ID do ticket e marca como usado
            Integer ticketId = qrCodeService.extractTicketId(qrCodeData);
            TicketModel usedTicket = ticketService.useTicket(ticketId);
            
            response.put("success", true);
            response.put("message", "Ingresso utilizado com sucesso");
            response.put("ticketId", usedTicket.getId());
            response.put("usedAt", usedTicket.getUsedAt());
            response.put("eventName", usedTicket.getEvent().getName());
            response.put("userName", usedTicket.getUser().getName());
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Erro ao utilizar ingresso: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/ticket/{ticketId}/qrcode")
    @PreAuthorize("hasRole('USER') or hasRole('ORGANIZER') or hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> getTicketQRCode(@PathVariable Integer ticketId) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            Optional<TicketModel> ticketOpt = ticketService.findTicketById(ticketId);
            
            if (ticketOpt.isEmpty()) {
                response.put("success", false);
                response.put("message", "Ingresso não encontrado");
                return ResponseEntity.notFound().build();
            }

            TicketModel ticket = ticketOpt.get();
            
            response.put("success", true);
            response.put("ticketId", ticket.getId());
            response.put("qrCodeImage", ticket.getQrCodeImage());
            response.put("qrCodeData", ticket.getQrCodeData());
            response.put("status", ticket.getStatus().toString());
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Erro ao obter QR Code: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}