package com.ingressos.api.model;

import java.io.Serial;
import java.io.Serializable;

import org.springframework.hateoas.RepresentationModel;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "tb_event_categories", catalog = "database_plataforma_evento")
public class EventCategorieModel extends RepresentationModel<EventCategorieModel> implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tb_event_categories")
    private Integer id;

    @Column(name = "tb_event_categories_name", nullable = false, unique = true)
    private String name;
}
