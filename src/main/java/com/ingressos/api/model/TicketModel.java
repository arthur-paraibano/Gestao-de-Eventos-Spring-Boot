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

    @Column(name = "tb_tickets_status", nullable = false)
    private String status; // VALID, USED, CANCELED

    @Column(name = "tb_tickets_created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "tb_tickets_updated_at", nullable = false)
    private LocalDateTime updatedAt;
}
