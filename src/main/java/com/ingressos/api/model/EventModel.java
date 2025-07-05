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
@Table(name = "tb_events", catalog = "database_plataforma_evento")
public class EventModel extends RepresentationModel<EventModel> implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tb_events")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "tb_events_fk_id_tb_users", referencedColumnName = "id_tb_users", nullable = false)
    private UserModel user;

    @ManyToOne
    @JoinColumn(name = "tb_events_fk_id_tb_addresses", referencedColumnName = "id_tb_addresses", nullable = false)
    private AddresseModel address;

    @Column(name = "tb_events_name", nullable = false)
    private String name;

    @Column(name = "tb_events_description")
    private String description;

    @Column(name = "tb_events_banner_image_url")
    private String bannerImageUrl;

    @Column(name = "tb_events_start_date", nullable = false)
    private LocalDateTime startDate;

    @Column(name = "tb_events_end_date", nullable = false)
    private LocalDateTime endDate;

    @Column(name = "tb_events_status", nullable = false)
    private String status; // Considere usar ENUM(DEFAULT = 'DRAFT', 'PUBLISHED', 'CANCELED', 'FINISHED')

    @Column(name = "tb_events_created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "tb_events_updated_at", nullable = false)
    private LocalDateTime updatedAt;
}
