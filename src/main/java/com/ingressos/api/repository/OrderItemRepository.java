package com.ingressos.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ingressos.api.model.OrderItemModel;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItemModel, Integer> {

}
