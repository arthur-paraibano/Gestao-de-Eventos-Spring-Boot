package com.ingressos.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ingressos.api.model.EventModel;

@Repository
public interface EventRepository extends JpaRepository<EventModel, Integer> {

}
