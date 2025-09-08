package com.ingressos.api.model;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

import org.springframework.hateoas.RepresentationModel;

import com.ingressos.api.enums.TicketStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "tb_tickets", catalog = "database_plataforma_evento")
public class TicketModel extends RepresentationModel<TicketModel> implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tb_tickets")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "tb_tickets_fk_id_tb_order_items", referencedColumnName = "id_tb_order_items", nullable = false)
    private OrderItemModel orderItem;

    @ManyToOne
    @JoinColumn(name = "tb_tickets_fk_id_tb_users", referencedColumnName = "id_tb_users", nullable = false)
    private UserModel user;

    @ManyToOne
    @JoinColumn(name = "tb_tickets_fk_id_tb_events", referencedColumnName = "id_tb_events", nullable = false)
    private EventModel event;

    @Column(name = "tb_tickets_qr_code_hash", nullable = false, unique = true)
    private String qrCodeHash;

    @Lob
    @Column(name = "tb_tickets_qr_code_image")
    private String qrCodeImage; // Base64 encoded QR code image

    @Column(name = "tb_tickets_qr_code_data", nullable = false)
    private String qrCodeData; // Raw QR code data

    @Enumerated(EnumType.STRING)
    @Column(name = "tb_tickets_status", nullable = false)
    private TicketStatus status = TicketStatus.AVAILABLE;

    @Column(name = "tb_tickets_used_at")
    private LocalDateTime usedAt;

    @Column(name = "tb_tickets_created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "tb_tickets_updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public TicketModel() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public OrderItemModel getOrderItem() {
        return orderItem;
    }

    public void setOrderItem(OrderItemModel orderItem) {
        this.orderItem = orderItem;
    }

    public UserModel getUser() {
        return user;
    }

    public void setUser(UserModel user) {
        this.user = user;
    }

    public EventModel getEvent() {
        return event;
    }

    public void setEvent(EventModel event) {
        this.event = event;
    }

    public String getQrCodeHash() {
        return qrCodeHash;
    }

    public void setQrCodeHash(String qrCodeHash) {
        this.qrCodeHash = qrCodeHash;
    }

    public String getQrCodeImage() {
        return qrCodeImage;
    }

    public void setQrCodeImage(String qrCodeImage) {
        this.qrCodeImage = qrCodeImage;
    }

    public String getQrCodeData() {
        return qrCodeData;
    }

    public void setQrCodeData(String qrCodeData) {
        this.qrCodeData = qrCodeData;
    }

    public TicketStatus getStatus() {
        return status;
    }

    public void setStatus(TicketStatus status) {
        this.status = status;
    }

    public LocalDateTime getUsedAt() {
        return usedAt;
    }

    public void setUsedAt(LocalDateTime usedAt) {
        this.usedAt = usedAt;
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
