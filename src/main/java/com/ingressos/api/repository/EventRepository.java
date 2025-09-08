package com.ingressos.api.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ingressos.api.model.EventModel;
import com.ingressos.api.model.UserModel;

@Repository
public interface EventRepository extends JpaRepository<EventModel, Integer> {
    // O nome da propriedade na entidade Event que se refere ao User (organizador)
    // deve corresponder ao que está depois de 'By'.
    // Ex: private User tbEventsFkIdTbUsers;
    List<EventModel> findByUser_Id(Long organizerId);

    @Query(value = "SELECT e FROM Event e WHERE e.tbEventsStatus = 'PUBLISHED' AND e.tbEventsStartDate > :currentDate ORDER BY e.tbEventsStartDate ASC", nativeQuery = true)
    List<EventModel> findPublishedAndUpcomingEvents(@Param("currentDate") LocalDateTime currentDate);
    
    // Método para relatórios
    List<EventModel> findByUserAndCreatedAtBetween(UserModel user, LocalDateTime startDate, LocalDateTime endDate);

}
