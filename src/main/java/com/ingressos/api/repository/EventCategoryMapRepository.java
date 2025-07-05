package com.ingressos.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ingressos.api.model.EventCategoryMapModel;

@Repository
public interface EventCategoryMapRepository extends JpaRepository<EventCategoryMapModel, Integer> {

}
