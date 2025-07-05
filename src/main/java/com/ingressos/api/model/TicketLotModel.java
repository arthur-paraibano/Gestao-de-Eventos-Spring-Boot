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
}
