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
@Table(name = "tb_addresses", catalog = "database_plataforma_evento")
public class AddresseModel extends RepresentationModel<AddresseModel> implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tb_addresses")
    private Integer id;

    @Column(name = "tb_addresses_street", nullable = false)
    private String street;

    @Column(name = "tb_addresses_number")
    private String number;

    @Column(name = "tb_addresses_complement")
    private String complement;

    @Column(name = "tb_addresses_city", nullable = false)
    private String city;

    @Column(name = "tb_addresses_state", nullable = false)
    private String state;

    @Column(name = "tb_addresses_zip_code", nullable = false)
    private String zipCode;

    @Column(name = "tb_addresses_latitude")
    private Double latitude;

    @Column(name = "tb_addresses_longitude")
    private Double longitude;
}
