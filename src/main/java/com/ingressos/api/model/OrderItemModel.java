package com.ingressos.api.model;

import java.io.Serial;
import java.io.Serializable;

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
@Table(name = "tb_order_items", catalog = "database_plataforma_evento")
public class OrderItemModel extends RepresentationModel<OrderItemModel> implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tb_order_items")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "tb_order_items_fk_id_tb_orders", referencedColumnName = "id_tb_orders", nullable = false)
    private OrderModel order;

    @ManyToOne
    @JoinColumn(name = "tb_order_items_fk_id_tb_ticket_lots", referencedColumnName = "id_tb_ticket_lots", nullable = false)
    private TicketLotModel ticketLot;

    @Column(name = "tb_order_items_quantity", nullable = false)
    private Integer quantity;

    @Column(name = "tb_order_items_unit_price", nullable = false)
    private Double unitPrice;

    public OrderItemModel() {
    }

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

    public TicketLotModel getTicketLot() {
        return ticketLot;
    }

    public void setTicketLot(TicketLotModel ticketLot) {
        this.ticketLot = ticketLot;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(Double unitPrice) {
        this.unitPrice = unitPrice;
    }
}
