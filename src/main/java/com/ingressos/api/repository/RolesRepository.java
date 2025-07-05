package com.ingressos.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ingressos.api.model.RolesModel;

@Repository
public interface RolesRepository extends JpaRepository<RolesModel, Integer> {

}
