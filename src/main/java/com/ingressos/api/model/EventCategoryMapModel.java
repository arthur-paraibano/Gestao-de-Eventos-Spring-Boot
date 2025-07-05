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
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "tb_event_category_map", catalog = "database_plataforma_evento")
public class EventCategoryMapModel extends RepresentationModel<EventCategoryMapModel> implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tb_event_category_map")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "tb_event_category_map_fk_id_tb_events", referencedColumnName = "id_tb_events", nullable = false)
    private EventModel event;

    @ManyToOne
    @JoinColumn(name = "tb_event_category_map_fk_id_tb_event_categories", referencedColumnName = "id_tb_event_categories", nullable = false)
    private EventCategorieModel eventCategory;
}
