package com.ingressos.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ingressos.api.model.OrderModel;

@Repository
public interface OrderRepository extends JpaRepository<OrderModel, Integer> {

}
