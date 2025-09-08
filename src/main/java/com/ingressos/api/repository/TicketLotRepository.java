package com.ingressos.api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ingressos.api.model.TicketLotModel;

@Repository
public interface TicketLotRepository extends JpaRepository<TicketLotModel, Integer> {
    List<TicketLotModel> findByTicketType_Id(Long ticketTypeId);
}
