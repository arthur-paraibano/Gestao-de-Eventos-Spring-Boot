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
@Table(name = "tb_ticket_lots", catalog = "database_plataforma_evento")
public class TicketLotModel extends RepresentationModel<TicketLotModel> implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tb_ticket_lots")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "tb_ticket_lots_fk_id_tb_ticket_types", referencedColumnName = "id_tb_ticket_types", nullable = false)
    private TicketTypeModel ticketType;

    @Column(name = "tb_ticket_lots_name", nullable = false)
    private String name;

    @Column(name = "tb_ticket_lots_price", nullable = false)
    private Double price;

    @Column(name = "tb_ticket_lots_quantity", nullable = false)
    private Integer quantity;

    @Column(name = "tb_ticket_lots_sale_start_date", nullable = false)
    private LocalDateTime saleStartDate;

    @Column(name = "tb_ticket_lots_sale_end_date", nullable = false)
    private LocalDateTime saleEndDate;

    public TicketLotModel() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public TicketTypeModel getTicketType() {
        return ticketType;
    }

    public void setTicketType(TicketTypeModel ticketType) {
        this.ticketType = ticketType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public LocalDateTime getSaleStartDate() {
        return saleStartDate;
    }

    public void setSaleStartDate(LocalDateTime saleStartDate) {
        this.saleStartDate = saleStartDate;
    }

    public LocalDateTime getSaleEndDate() {
        return saleEndDate;
    }

    public void setSaleEndDate(LocalDateTime saleEndDate) {
        this.saleEndDate = saleEndDate;
    }
}
