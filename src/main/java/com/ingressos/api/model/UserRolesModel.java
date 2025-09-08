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
@Table(name = "tb_user_roles", catalog = "database_plataforma_evento")
public class UserRolesModel extends RepresentationModel<UserRolesModel> implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tb_user_roles")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "tb_user_roles_fk_id_tb_users", referencedColumnName = "id_tb_users", nullable = false)
    private UserModel user;

    @ManyToOne
    @JoinColumn(name = "tb_user_roles_fk_id_tb_roles", referencedColumnName = "id_tb_roles", nullable = false)
    private RolesModel roles;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public UserModel getUser() {
        return user;
    }

    public void setUser(UserModel user) {
        this.user = user;
    }

    public RolesModel getRoles() {
        return roles;
    }

    public void setRoles(RolesModel roles) {
        this.roles = roles;
    }
}
