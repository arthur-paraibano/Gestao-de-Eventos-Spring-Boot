package com.ingressos.api.model;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.hateoas.RepresentationModel;

import com.ingressos.api.enums.PaymentStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.RequiredArgsConstructor;

@Entity
@RequiredArgsConstructor
@Table(name = "tb_payments", catalog = "database_plataforma_evento")
public class PaymentModel extends RepresentationModel<PaymentModel> implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tb_payments")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "tb_payments_fk_id_tb_orders", referencedColumnName = "id_tb_orders", nullable = false)
    private OrderModel order;

    @Column(name = "tb_payments_amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;

    @Column(name = "tb_payments_payment_method", nullable = false)
    private String paymentMethod; // CREDIT_CARD, DEBIT_CARD, PIX, BOLETO

    @Column(name = "tb_payments_transaction_id", nullable = false, unique = true)
    private String transactionId;

    @Column(name = "tb_payments_external_payment_id")
    private String externalPaymentId; // ID do pagamento no gateway externo

    @Enumerated(EnumType.STRING)
    @Column(name = "tb_payments_status", nullable = false)
    private PaymentStatus status = PaymentStatus.PENDING;

    @Column(name = "tb_payments_gateway", nullable = false)
    private String gateway = "MERCADO_PAGO";

    @Column(name = "tb_payments_payment_details")
    private String paymentDetails; // JSON format

    @Column(name = "tb_payments_paid_at")
    private LocalDateTime paidAt;

    @Column(name = "tb_payments_refund_reason")
    private String refundReason;

    @Column(name = "tb_payments_refunded_at")
    private LocalDateTime refundedAt;

    @Column(name = "tb_payments_created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "tb_payments_updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public OrderModel getOrder() {
        return order;
    }

    public void setOrder(OrderModel order) {
        this.order = order;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getExternalPaymentId() {
        return externalPaymentId;
    }

    public void setExternalPaymentId(String externalPaymentId) {
        this.externalPaymentId = externalPaymentId;
    }

    public PaymentStatus getStatus() {
        return status;
    }

    public void setStatus(PaymentStatus status) {
        this.status = status;
    }

    public String getGateway() {
        return gateway;
    }

    public void setGateway(String gateway) {
        this.gateway = gateway;
    }

    public String getPaymentDetails() {
        return paymentDetails;
    }

    public void setPaymentDetails(String paymentDetails) {
        this.paymentDetails = paymentDetails;
    }

    public LocalDateTime getPaidAt() {
        return paidAt;
    }

    public void setPaidAt(LocalDateTime paidAt) {
        this.paidAt = paidAt;
    }

    public String getRefundReason() {
        return refundReason;
    }

    public void setRefundReason(String refundReason) {
        this.refundReason = refundReason;
    }

    public LocalDateTime getRefundedAt() {
        return refundedAt;
    }

    public void setRefundedAt(LocalDateTime refundedAt) {
        this.refundedAt = refundedAt;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
