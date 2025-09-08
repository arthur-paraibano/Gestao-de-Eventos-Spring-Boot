package com.ingressos.api.controller;

import com.ingressos.api.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/reports")
@CrossOrigin(origins = "*")
public class ReportController {

    @Autowired
    private ReportService reportService;

    // Relatório de vendas por evento
    @GetMapping("/sales/event/{eventId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('ORGANIZER')")
    public ResponseEntity<?> generateEventSalesReport(
            @PathVariable Integer eventId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        
        try {
            byte[] pdfBytes = reportService.generateEventSalesReport(eventId, startDate, endDate);
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", "relatorio-vendas-evento-" + eventId + ".pdf");
            headers.setContentLength(pdfBytes.length);
            
            return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Erro ao gerar relatório: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // Relatório geral de vendas
    @GetMapping("/sales/general")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> generateGeneralSalesReport(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        
        try {
            byte[] pdfBytes = reportService.generateGeneralSalesReport(startDate, endDate);
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", "relatorio-vendas-geral.pdf");
            headers.setContentLength(pdfBytes.length);
            
            return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Erro ao gerar relatório: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // Relatório de eventos por organizador
    @GetMapping("/organizer/{organizerId}")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('ORGANIZER') and #organizerId == authentication.principal.id)")
    public ResponseEntity<?> generateOrganizerEventsReport(
            @PathVariable Integer organizerId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        
        try {
            byte[] pdfBytes = reportService.generateOrganizerEventsReport(organizerId, startDate, endDate);
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", "relatorio-organizador-" + organizerId + ".pdf");
            headers.setContentLength(pdfBytes.length);
            
            return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Erro ao gerar relatório: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // Relatório de usuários cadastrados
    @GetMapping("/users")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> generateUsersReport(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        
        try {
            byte[] pdfBytes = reportService.generateUsersReport(startDate, endDate);
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", "relatorio-usuarios.pdf");
            headers.setContentLength(pdfBytes.length);
            
            return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Erro ao gerar relatório: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // Relatório financeiro
    @GetMapping("/financial")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> generateFinancialReport(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        
        try {
            byte[] pdfBytes = reportService.generateFinancialReport(startDate, endDate);
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", "relatorio-financeiro.pdf");
            headers.setContentLength(pdfBytes.length);
            
            return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Erro ao gerar relatório: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // Relatório de ingressos por evento
    @GetMapping("/tickets/event/{eventId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('ORGANIZER')")
    public ResponseEntity<?> generateTicketsReport(@PathVariable Integer eventId) {
        try {
            byte[] pdfBytes = reportService.generateTicketsReport(eventId);
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", "relatorio-ingressos-" + eventId + ".pdf");
            headers.setContentLength(pdfBytes.length);
            
            return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Erro ao gerar relatório: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // Endpoint para visualizar relatório no navegador (sem download)
    @GetMapping("/sales/event/{eventId}/view")
    @PreAuthorize("hasRole('ADMIN') or hasRole('ORGANIZER')")
    public ResponseEntity<?> viewEventSalesReport(
            @PathVariable Integer eventId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        
        try {
            byte[] pdfBytes = reportService.generateEventSalesReport(eventId, startDate, endDate);
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("inline", "relatorio-vendas-evento-" + eventId + ".pdf");
            
            return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Erro ao gerar relatório: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // Endpoint para visualizar relatório geral no navegador
    @GetMapping("/sales/general/view")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> viewGeneralSalesReport(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        
        try {
            byte[] pdfBytes = reportService.generateGeneralSalesReport(startDate, endDate);
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("inline", "relatorio-vendas-geral.pdf");
            
            return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Erro ao gerar relatório: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // Endpoint para obter estatísticas rápidas (JSON)
    @GetMapping("/stats/quick")
    @PreAuthorize("hasRole('ADMIN') or hasRole('ORGANIZER')")
    public ResponseEntity<Map<String, Object>> getQuickStats(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            // Se as datas não forem fornecidas, usar os últimos 30 dias
            if (startDate == null) {
                startDate = LocalDateTime.now().minusDays(30);
            }
            if (endDate == null) {
                endDate = LocalDateTime.now();
            }
            
            // TODO: Implementar lógica para obter estatísticas rápidas
            // Por enquanto, retornar dados mock
            response.put("success", true);
            response.put("period", Map.of(
                "startDate", startDate.toString(),
                "endDate", endDate.toString()
            ));
            response.put("stats", Map.of(
                "totalOrders", 0,
                "totalRevenue", 0.0,
                "totalTickets", 0,
                "uniqueCustomers", 0,
                "activeEvents", 0
            ));
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Erro ao obter estatísticas: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // Endpoint para listar tipos de relatórios disponíveis
    @GetMapping("/types")
    @PreAuthorize("hasRole('ADMIN') or hasRole('ORGANIZER')")
    public ResponseEntity<Map<String, Object>> getReportTypes() {
        Map<String, Object> response = new HashMap<>();
        
        try {
            Map<String, Object> reportTypes = new HashMap<>();
            
            reportTypes.put("sales", Map.of(
                "name", "Relatórios de Vendas",
                "endpoints", Map.of(
                    "event", "/api/reports/sales/event/{eventId}",
                    "general", "/api/reports/sales/general"
                ),
                "requiredParams", List.of("startDate", "endDate"),
                "permissions", List.of("ADMIN", "ORGANIZER")
            ));
            
            reportTypes.put("organizer", Map.of(
                "name", "Relatório do Organizador",
                "endpoint", "/api/reports/organizer/{organizerId}",
                "requiredParams", List.of("startDate", "endDate"),
                "permissions", List.of("ADMIN", "ORGANIZER")
            ));
            
            reportTypes.put("users", Map.of(
                "name", "Relatório de Usuários",
                "endpoint", "/api/reports/users",
                "requiredParams", List.of("startDate", "endDate"),
                "permissions", List.of("ADMIN")
            ));
            
            reportTypes.put("financial", Map.of(
                "name", "Relatório Financeiro",
                "endpoint", "/api/reports/financial",
                "requiredParams", List.of("startDate", "endDate"),
                "permissions", List.of("ADMIN")
            ));
            
            reportTypes.put("tickets", Map.of(
                "name", "Relatório de Ingressos",
                "endpoint", "/api/reports/tickets/event/{eventId}",
                "requiredParams", List.of(),
                "permissions", List.of("ADMIN", "ORGANIZER")
            ));
            
            response.put("success", true);
            response.put("reportTypes", reportTypes);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Erro ao obter tipos de relatórios: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // Endpoint para verificar status do serviço de relatórios
    @GetMapping("/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> getReportServiceStatus() {
        Map<String, Object> response = new HashMap<>();
        
        try {
            response.put("success", true);
            response.put("status", "active");
            response.put("timestamp", System.currentTimeMillis());
            response.put("supportedFormats", List.of("PDF"));
            response.put("version", "1.0.0");
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Erro ao verificar status: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}