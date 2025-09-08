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

@Entity
@SuppressWarnings("unused")
@Table(name = "tb_orders", catalog = "database_plataforma_evento")
public class OrderModel extends RepresentationModel<OrderModel> implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tb_orders")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "tb_orders_fk_id_tb_users", referencedColumnName = "id_tb_users", nullable = false)
    private UserModel user;

    @ManyToOne
    @JoinColumn(name = "tb_orders_fk_id_tb_events", referencedColumnName = "id_tb_events", nullable = false)
    private EventModel event;

    @ManyToOne
    @JoinColumn(name = "tb_orders_fk_id_tb_tickets", referencedColumnName = "id_tb_tickets", nullable = false)
    private TicketModel ticket;

    @Column(name = "tb_orders_quantity", nullable = false)
    private Integer quantity;

    @Column(name = "tb_orders_total_amount", nullable = false)
    private Double totalAmount;

    @Column(name = "tb_orders_status", nullable = false)
    private String status; // PENDING, PAID, CANCELED, REFUNDED

    @Column(name = "tb_orders_created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "tb_orders_updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public OrderModel() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public TicketModel getTicket() {
        return ticket;
    }

    public void setTicket(TicketModel ticket) {
        this.ticket = ticket;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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
