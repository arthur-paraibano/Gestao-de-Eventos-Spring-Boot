package com.ingressos.api.model;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

import org.springframework.hateoas.RepresentationModel;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "tb_payments", catalog = "database_plataforma_evento")
public class PaymentModel extends RepresentationModel<PaymentModel> implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tb_payments")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "tb_payments_fk_id_tb_orders", referencedColumnName = "id_tb_orders", nullable = false)
    private OrderModel order;

    @Column(name = "tb_payments_gateway", nullable = false)
    private String gateway; // STRIPE, MERCADO_PAGO, PIX

    @Column(name = "tb_payments_gateway_transaction_id", nullable = false)
    private String gatewayTransactionId;

    @Column(name = "tb_payments_status", nullable = false)
    private String status; // PENDING, APPROVED, REJECTED, REFUNDED

    @Column(name = "tb_payments_payment_details")
    private String paymentDetails; // JSON format

    @Column(name = "tb_payments_created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "tb_payments_updated_at", nullable = false)
    private LocalDateTime updatedAt;
}
