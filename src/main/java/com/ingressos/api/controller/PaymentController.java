package com.ingressos.api.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpServerErrorException.InternalServerError;

import com.ingressos.api.dto.GeneralDto;
import com.ingressos.api.enums.PaymentStatus;
import com.ingressos.api.exceptions.InternalServer;
import com.ingressos.api.infra.exception.RestMessage;
import com.ingressos.api.model.OrderModel;
import com.ingressos.api.model.PaymentModel;
import com.ingressos.api.service.OrderService;
import com.ingressos.api.service.PaymentService;
import com.ingressos.api.util.InternationalizationUtil;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/payment", produces = {"application/json"})
@Tag(name = "Payment API", description = "Payment Controller")
public class PaymentController {

    @Autowired
    private PaymentService service;
    @Autowired
    private OrderService orderService;
    @Autowired
    private InternationalizationUtil messageInternationalization;

    @GetMapping("/all")
    @Operation(summary = "Find all payments", description = "Find all payments")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful operation", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = RestMessage.class))}),
        @ApiResponse(responseCode = "404", description = "Payment not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = RestMessage.class))})
    })
    public ResponseEntity<List<PaymentModel>> findAll() {
        try {
            List<PaymentModel> EquipamentModels = service.findAll();
            for (PaymentModel uModel : EquipamentModels) {
                Integer id = uModel.getId();
                GeneralDto dtoId = new GeneralDto(id);
                uModel.add(linkTo(methodOn(PaymentController.class).getById(dtoId)).withSelfRel());
            }
            return ResponseEntity.status(HttpStatus.OK).body(EquipamentModels);
        } catch (InternalServerError e) {
            throw new InternalServer(e.getMessage());
        } catch (Exception e) {
            throw new InternalServer(e.getMessage());
        }
    }

    @PostMapping("/id")
    @Operation(summary = "Find payment by id", description = "Find payment by id")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful operation", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = RestMessage.class))}),
        @ApiResponse(responseCode = "204", description = "Payment not found", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = RestMessage.class))}),
        @ApiResponse(responseCode = "500", description = "Internal server error", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = RestMessage.class))})
    })
    public ResponseEntity<PaymentModel> getById(@Valid @RequestBody GeneralDto id) {
        try {
            PaymentModel uModel = service.findById(id);
            uModel.add(linkTo(methodOn(PaymentController.class).findAll()).withRel("findAll"));
            return ResponseEntity.status(HttpStatus.OK).body(uModel);
        } catch (InternalServerError e) {
            throw new InternalServer(e.getMessage());
        } catch (Exception e) {
            throw new InternalServer(e.getMessage());
        }
    }

    // Novos endpoints para integração com Mercado Pago
    @PostMapping("/create-preference")
    @PreAuthorize("hasRole('USER') or hasRole('ORGANIZER') or hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> createPaymentPreference(@RequestBody Map<String, Object> request) {
        Map<String, Object> response = new HashMap<>();

        try {
            Integer orderId = (Integer) request.get("orderId");
            String paymentMethod = (String) request.get("paymentMethod");
            String description = (String) request.get("description");

            if (orderId == null) {
                response.put("success", false);
                response.put("message", "ID do pedido é obrigatório");
                return ResponseEntity.badRequest().body(response);
            }

            Optional<OrderModel> orderOpt = orderService.findById(new GeneralDto(orderId));
            if (orderOpt.isEmpty()) {
                response.put("success", false);
                response.put("message", "Pedido não encontrado");
                return ResponseEntity.notFound().build();
            }

            OrderModel order = orderOpt.get();
            // Convertendo Double para BigDecimal
            BigDecimal amount = new BigDecimal(order.getTotalAmount().toString());

            // Cria o pagamento no banco de dados
            PaymentModel payment = service.createPayment(order, amount, paymentMethod != null ? paymentMethod : "CREDIT_CARD");

            // Cria a preferência no Mercado Pago
            String preferenceId = service.createMercadoPagoPreference(order, amount, description != null ? description : "Compra de ingressos");

            response.put("success", true);
            response.put("preferenceId", preferenceId);
            response.put("paymentId", payment.getId());
            response.put("transactionId", payment.getTransactionId());
            response.put("amount", amount);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Erro ao criar preferência de pagamento: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PostMapping("/webhook/mercadopago")
    public ResponseEntity<String> handleMercadoPagoWebhook(@RequestBody Map<String, Object> notification) {
        try {
            String type = (String) notification.get("type");

            if ("payment".equals(type)) {
                Map<String, Object> data = (Map<String, Object>) notification.get("data");
                String paymentId = data.get("id").toString();
                String status = (String) data.get("status");

                if (status != null) {
                    PaymentStatus paymentStatus = service.mapMercadoPagoStatus(status);

                    // Atualiza o status do pagamento
                    Optional<PaymentModel> paymentOpt = service.findByExternalPaymentId(paymentId);
                    if (paymentOpt.isPresent()) {
                        service.updatePaymentStatus(paymentOpt.get().getTransactionId(), paymentStatus, paymentId);
                    }
                }
            }

            return ResponseEntity.ok("OK");

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error processing webhook");
        }
    }

    @GetMapping("/order/{orderId}")
    @PreAuthorize("hasRole('USER') or hasRole('ORGANIZER') or hasRole('ADMIN')")
    public ResponseEntity<List<PaymentModel>> getPaymentsByOrder(@PathVariable Integer orderId) {
        try {
            GeneralDto dto = new GeneralDto(orderId);
            Optional<OrderModel> orderOpt = orderService.findById(dto);

            if (orderOpt.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            OrderModel order = orderOpt.get();
            List<PaymentModel> payments = service.findByOrder(order);
            return ResponseEntity.ok(payments);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/transaction/{transactionId}")
    @PreAuthorize("hasRole('USER') or hasRole('ORGANIZER') or hasRole('ADMIN')")
    public ResponseEntity<PaymentModel> getPaymentByTransactionId(@PathVariable String transactionId) {
        try {
            Optional<PaymentModel> paymentOpt = service.findByTransactionId(transactionId);

            if (paymentOpt.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            return ResponseEntity.ok(paymentOpt.get());

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/update-status")
    @PreAuthorize("hasRole('ADMIN') or hasRole('ORGANIZER')")
    public ResponseEntity<Map<String, Object>> updatePaymentStatus(@RequestBody Map<String, Object> request) {
        Map<String, Object> response = new HashMap<>();

        try {
            String transactionId = (String) request.get("transactionId");
            String status = (String) request.get("status");
            String externalId = (String) request.get("externalId");

            if (transactionId == null || status == null) {
                response.put("success", false);
                response.put("message", "Transaction ID e status são obrigatórios");
                return ResponseEntity.badRequest().body(response);
            }

            PaymentStatus paymentStatus = PaymentStatus.valueOf(status.toUpperCase());
            PaymentModel updatedPayment = service.updatePaymentStatus(transactionId, paymentStatus, externalId);

            response.put("success", true);
            response.put("payment", updatedPayment);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Erro ao atualizar status do pagamento: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // Páginas de retorno do Mercado Pago
    @GetMapping("/success")
    public ResponseEntity<String> paymentSuccess(@RequestParam(required = false) String collection_id,
            @RequestParam(required = false) String collection_status,
            @RequestParam(required = false) String external_reference) {
        return ResponseEntity.ok("Pagamento realizado com sucesso!");
    }

    @GetMapping("/pending")
    public ResponseEntity<String> paymentPending(@RequestParam(required = false) String collection_id,
            @RequestParam(required = false) String collection_status,
            @RequestParam(required = false) String external_reference) {
        return ResponseEntity.ok("Pagamento pendente de aprovação.");
    }

    @GetMapping("/failure")
    public ResponseEntity<String> paymentFailure(@RequestParam(required = false) String collection_id,
            @RequestParam(required = false) String collection_status,
            @RequestParam(required = false) String external_reference) {
        return ResponseEntity.ok("Falha no pagamento. Tente novamente.");
    }
}
