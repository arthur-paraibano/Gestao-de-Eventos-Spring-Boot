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

    @Column(name = "tb_orders_total_amount", nullable = false)
    private Double totalAmount;

    @Column(name = "tb_orders_status", nullable = false)
    private String status; // PENDING, PAID, CANCELED, REFUNDED

    @Column(name = "tb_orders_created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "tb_orders_updated_at", nullable = false)
    private LocalDateTime updatedAt;

}
