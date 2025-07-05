package com.ingressos.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ingressos.api.model.AddresseModel;

@Repository
public interface AddresseRepository extends JpaRepository<AddresseModel, Integer> {

}
