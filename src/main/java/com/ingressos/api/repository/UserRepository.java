package com.ingressos.api.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ingressos.api.model.UserModel;

@Repository
public interface UserRepository extends JpaRepository<UserModel, Integer> {
    Optional<UserModel> findByEmail(String email);

    boolean existsByEmail(String email);
    
    // Método para relatórios
    List<UserModel> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);
}
