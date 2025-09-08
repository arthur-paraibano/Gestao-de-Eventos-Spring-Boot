package com.ingressos.api.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ingressos.api.model.EventModel;
import com.ingressos.api.model.OrderModel;

@Repository
public interface OrderRepository extends JpaRepository<OrderModel, Integer> {

    List<OrderModel> findByUser_IdOrderByCreatedAtDesc(Long userId);

    List<OrderModel> findByEvent_IdOrderByCreatedAtDesc(Long eventId);

    List<OrderModel> findByTicket_IdOrderByCreatedAtDesc(Long ticketId);
    
    // Métodos para relatórios
    List<OrderModel> findByEventAndCreatedAtBetween(EventModel event, LocalDateTime startDate, LocalDateTime endDate);
    
    List<OrderModel> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);
}
