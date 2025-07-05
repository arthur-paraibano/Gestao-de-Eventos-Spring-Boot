package com.ingressos.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ingressos.api.model.EventCategorieModel;

@Repository
public interface EventCategorieRepository extends JpaRepository<EventCategorieModel, Integer> {

}
