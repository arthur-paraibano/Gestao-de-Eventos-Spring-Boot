package com.ingressos.api.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ingressos.api.enums.TicketStatus;
import com.ingressos.api.model.EventModel;
import com.ingressos.api.model.TicketModel;
import com.ingressos.api.model.UserModel;

@Repository
public interface TicketRepository extends JpaRepository<TicketModel, Integer> {
    Optional<TicketModel> findByQrCodeHash(String qrCodeHash);

    List<TicketModel> findByUser_Id(Long userId);
    
    List<TicketModel> findByUser(UserModel user);
    
    List<TicketModel> findByEvent(EventModel event);
    
    List<TicketModel> findByUserAndEvent(UserModel user, EventModel event);
    
    List<TicketModel> findByStatus(TicketStatus status);
    
    List<TicketModel> findByEventAndStatus(EventModel event, TicketStatus status);
    
    long countByEventAndStatus(EventModel event, TicketStatus status);
    
    Optional<TicketModel> findByQrCodeData(String qrCodeData);
}
