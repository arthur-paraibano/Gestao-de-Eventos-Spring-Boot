package com.ingressos.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ingressos.api.model.PaymentModel;

@Repository
public interface PaymentRepository extends JpaRepository<PaymentModel, Integer> {

}
