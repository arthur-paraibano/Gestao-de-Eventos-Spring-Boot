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
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "tb_users", catalog = "database_plataforma_evento")
public class UserModel extends RepresentationModel<UserModel> implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tb_users")
    private Integer id;

    @Column(name = "tb_users_name", nullable = false)
    private String name;

    @Column(name = "tb_users_email", nullable = false, unique = true)
    private String email;

    @Column(name = "tb_users_password_hash", nullable = false)
    private String passwordHash;

    @Column(name = "tb_users_phone_number")
    private String phoneNumber;

    @Column(name = "tb_users_created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "tb_users_updated_at", nullable = false)
    private LocalDateTime updatedAt;
}
