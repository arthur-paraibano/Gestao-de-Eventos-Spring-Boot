package com.ingressos.api.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ingressos.api.enums.PaymentStatus;
import com.ingressos.api.model.OrderModel;
import com.ingressos.api.model.PaymentModel;

@Repository
public interface PaymentRepository extends JpaRepository<PaymentModel, Integer> {
    
    Optional<PaymentModel> findByTransactionId(String transactionId);
    
    Optional<PaymentModel> findByExternalPaymentId(String externalPaymentId);
    
    List<PaymentModel> findByOrder(OrderModel order);
    
    List<PaymentModel> findByStatus(PaymentStatus status);
    
    List<PaymentModel> findByGateway(String gateway);
    
    List<PaymentModel> findByPaymentMethod(String paymentMethod);
    
    List<PaymentModel> findByOrderAndStatus(OrderModel order, PaymentStatus status);
    
    // Método para relatórios
    List<PaymentModel> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);
}
