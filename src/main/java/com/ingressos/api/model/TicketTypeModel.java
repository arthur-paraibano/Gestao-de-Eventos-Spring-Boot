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
@Table(name = "tb_ticket_types", catalog = "database_plataforma_evento")
public class TicketTypeModel extends RepresentationModel<TicketTypeModel> implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tb_ticket_types")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "tb_ticket_types_fk_id_tb_events", referencedColumnName = "id_tb_events", nullable = false)
    private EventModel event;

    @Column(name = "tb_ticket_types_name", nullable = false)
    private String name;

    @Column(name = "tb_ticket_types_description")
    private String description;

    @Column(name = "tb_ticket_types_total_quantity", nullable = false)
    private Integer totalQuantity;

    public TicketTypeModel() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public EventModel getEvent() {
        return event;
    }

    public void setEvent(EventModel event) {
        this.event = event;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getTotalQuantity() {
        return totalQuantity;
    }

    public void setTotalQuantity(Integer totalQuantity) {
        this.totalQuantity = totalQuantity;
    }
}
