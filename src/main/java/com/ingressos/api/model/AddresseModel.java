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

    public AddresseModel() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getComplement() {
        return complement;
    }

    public void setComplement(String complement) {
        this.complement = complement;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }
}
