package com.ingressos.api.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ingressos.api.dto.GeneralDto;
import com.ingressos.api.model.EventModel;
import com.ingressos.api.model.OrderModel;
import com.ingressos.api.model.TicketModel;
import com.ingressos.api.model.UserModel;
import com.ingressos.api.service.EventService;
import com.ingressos.api.service.NotificationService;
import com.ingressos.api.service.OrderService;
import com.ingressos.api.service.TicketService;
import com.ingressos.api.service.UserService;

@RestController
@RequestMapping("/api/notifications")
@CrossOrigin(origins = "*")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserService userService;

    @Autowired
    private EventService eventService;

    @Autowired
    private TicketService ticketService;

    // Enviar confirmação de compra
    @PostMapping("/purchase-confirmation")
    @PreAuthorize("hasRole('ADMIN') or hasRole('ORGANIZER')")
    public ResponseEntity<Map<String, Object>> sendPurchaseConfirmation(@RequestParam GeneralDto orderId) {
        Map<String, Object> response = new HashMap<>();

        try {
            Optional<OrderModel> orderOptional = orderService.findById(orderId);

            if (orderOptional.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
            }

            OrderModel order = orderOptional.get();

            // Modificação aqui: usar o ticket do order diretamente
            List<TicketModel> tickets = new ArrayList<>();
            if (order.getTicket() != null) {
                tickets.add(order.getTicket());
            }

            CompletableFuture<Boolean> result = notificationService.sendPurchaseConfirmation(order, tickets);

            response.put("success", true);
            response.put("message", "Notificação de confirmação de compra enviada");
            response.put("orderId", orderId);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Erro ao enviar notificação: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // Enviar notificação de pagamento aprovado
    @PostMapping("/payment-approved")
    @PreAuthorize("hasRole('ADMIN') or hasRole('ORGANIZER')")
    public ResponseEntity<Map<String, Object>> sendPaymentApproved(@RequestParam Integer orderId) {
        Map<String, Object> response = new HashMap<>();

        try {
            GeneralDto orderDto = new GeneralDto(orderId);
            Optional<OrderModel> orderOptional = orderService.findById(orderDto);

            if (orderOptional.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
            }

            OrderModel order = orderOptional.get();

            CompletableFuture<Boolean> result = notificationService.sendPaymentApproved(order);

            response.put("success", true);
            response.put("message", "Notificação de pagamento aprovado enviada");
            response.put("orderId", orderId);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Erro ao enviar notificação: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // Enviar notificação de pagamento rejeitado
    @PostMapping("/payment-rejected")
    @PreAuthorize("hasRole('ADMIN') or hasRole('ORGANIZER')")
    public ResponseEntity<Map<String, Object>> sendPaymentRejected(
            @RequestParam Integer orderId,
            @RequestParam(required = false) String reason) {
        Map<String, Object> response = new HashMap<>();

        try {
            GeneralDto orderDto = new GeneralDto(orderId);
            Optional<OrderModel> orderOptional = orderService.findById(orderDto);

            if (orderOptional.isEmpty()) {
                response.put("success", false);
                response.put("message", "Pedido não encontrado");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }

            OrderModel order = orderOptional.get();
            CompletableFuture<Boolean> result = notificationService.sendPaymentRejected(order, reason);

            response.put("success", true);
            response.put("message", "Notificação de pagamento rejeitado enviada");
            response.put("orderId", orderId);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Erro ao enviar notificação: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // Enviar lembrete de evento
    @PostMapping("/event-reminder")
    @PreAuthorize("hasRole('ADMIN') or hasRole('ORGANIZER')")
    public ResponseEntity<Map<String, Object>> sendEventReminder(
            @RequestParam Integer userId,
            @RequestParam Integer eventId,
            @RequestParam Integer ticketId) {
        Map<String, Object> response = new HashMap<>();

        try {
            GeneralDto userDto = new GeneralDto(userId);
            GeneralDto eventDto = new GeneralDto(eventId);

            UserModel user = userService.findById(userDto);
            EventModel event = eventService.findById(eventDto);
            Optional<TicketModel> ticketOpt = ticketService.findTicketById(ticketId);

            if (!ticketOpt.isPresent()) {
                response.put("success", false);
                response.put("message", "Ingresso não encontrado");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }

            TicketModel ticket = ticketOpt.get();

            CompletableFuture<Boolean> result = notificationService.sendEventReminder(user, event, ticket);

            response.put("success", true);
            response.put("message", "Lembrete de evento enviado");
            response.put("userId", userId);
            response.put("eventId", eventId);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Erro ao enviar lembrete: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // Enviar notificação de cancelamento de evento
    @PostMapping("/event-cancellation")
    @PreAuthorize("hasRole('ADMIN') or hasRole('ORGANIZER')")
    public ResponseEntity<Map<String, Object>> sendEventCancellation(
            @RequestParam Integer userId,
            @RequestParam Integer eventId,
            @RequestParam Integer orderId) {
        Map<String, Object> response = new HashMap<>();

        try {
            GeneralDto userDto = new GeneralDto(userId);
            GeneralDto eventDto = new GeneralDto(eventId);
            GeneralDto orderDto = new GeneralDto(orderId);

            UserModel user = userService.findById(userDto);
            EventModel event = eventService.findById(eventDto);
            Optional<OrderModel> orderOptional = orderService.findById(orderDto);

            if (orderOptional.isEmpty()) {
                response.put("success", false);
                response.put("message", "Pedido não encontrado");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }

            OrderModel order = orderOptional.get();
            CompletableFuture<Boolean> result = notificationService.sendEventCancellation(user, event, order);

            response.put("success", true);
            response.put("message", "Notificação de cancelamento enviada");
            response.put("userId", userId);
            response.put("eventId", eventId);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Erro ao enviar notificação de cancelamento: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // Enviar email de boas-vindas
    @PostMapping("/welcome")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> sendWelcomeEmail(@RequestParam Integer userId) {
        Map<String, Object> response = new HashMap<>();

        try {
            GeneralDto userDto = new GeneralDto(userId);
            UserModel user = userService.findById(userDto);
            CompletableFuture<Boolean> result = notificationService.sendWelcomeEmail(user);

            response.put("success", true);
            response.put("message", "Email de boas-vindas enviado");
            response.put("userId", userId);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Erro ao enviar email de boas-vindas: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // Enviar email de recuperação de senha
    @PostMapping("/password-reset")
    public ResponseEntity<Map<String, Object>> sendPasswordReset(
            @RequestParam Integer userId,
            @RequestParam String resetToken) {
        Map<String, Object> response = new HashMap<>();

        try {
            GeneralDto userDto = new GeneralDto(userId);
            UserModel user = userService.findById(userDto);
            CompletableFuture<Boolean> result = notificationService.sendPasswordReset(user, resetToken);

            response.put("success", true);
            response.put("message", "Email de recuperação de senha enviado");
            response.put("userId", userId);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Erro ao enviar email de recuperação: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // Enviar notificação push
    @PostMapping("/push")
    @PreAuthorize("hasRole('ADMIN') or hasRole('ORGANIZER')")
    public ResponseEntity<Map<String, Object>> sendPushNotification(
            @RequestParam String userId,
            @RequestParam String title,
            @RequestParam String message,
            @RequestParam(required = false) String data) {
        Map<String, Object> response = new HashMap<>();

        try {
            CompletableFuture<Boolean> result = notificationService.sendPushNotification(userId, title, message, data);

            response.put("success", true);
            response.put("message", "Notificação push enviada");
            response.put("userId", userId);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Erro ao enviar notificação push: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // Enviar SMS
    @PostMapping("/sms")
    @PreAuthorize("hasRole('ADMIN') or hasRole('ORGANIZER')")
    public ResponseEntity<Map<String, Object>> sendSMS(
            @RequestParam String phoneNumber,
            @RequestParam String message) {
        Map<String, Object> response = new HashMap<>();

        try {
            CompletableFuture<Boolean> result = notificationService.sendSMS(phoneNumber, message);

            response.put("success", true);
            response.put("message", "SMS enviado");
            response.put("phoneNumber", phoneNumber);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Erro ao enviar SMS: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // Enviar notificações em lote para evento
    @PostMapping("/bulk/event/{eventId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('ORGANIZER')")
    public ResponseEntity<Map<String, Object>> sendBulkEventNotifications(
            @PathVariable Integer eventId,
            @RequestParam String type,
            @RequestParam(required = false) String customMessage) {
        Map<String, Object> response = new HashMap<>();

        try {
            GeneralDto eventDto = new GeneralDto(eventId);
            EventModel event = eventService.findById(eventDto);
            List<OrderModel> orders = orderService.findByEvent(eventDto);

            int successCount = 0;
            int totalCount = orders.size();

            for (OrderModel order : orders) {
                try {
                    switch (type.toLowerCase()) {
                        case "reminder":
                            // Usar o ticket associado ao pedido
                            if (order.getTicket() != null) {
                                notificationService.sendEventReminder(order.getUser(), event, order.getTicket());
                                successCount++;
                            }
                            break;
                        case "cancellation":
                            notificationService.sendEventCancellation(order.getUser(), event, order);
                            successCount++;
                            break;
                        default:
                            break;
                    }
                } catch (Exception e) {
                    // Log error but continue with other notifications
                }
            }

            response.put("success", true);
            response.put("message", String.format("Notificações enviadas: %d de %d", successCount, totalCount));
            response.put("eventId", eventId);
            response.put("type", type);
            response.put("successCount", successCount);
            response.put("totalCount", totalCount);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Erro ao enviar notificações em lote: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // Verificar status de notificação
    @GetMapping("/status")
    @PreAuthorize("hasRole('ADMIN') or hasRole('ORGANIZER')")
    public ResponseEntity<Map<String, Object>> getNotificationStatus() {
        Map<String, Object> response = new HashMap<>();

        try {
            // TODO: Implementar verificação de status do serviço de email
            response.put("emailService", "active");
            response.put("pushService", "inactive"); // Placeholder
            response.put("smsService", "inactive"); // Placeholder
            response.put("timestamp", System.currentTimeMillis());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Erro ao verificar status: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
