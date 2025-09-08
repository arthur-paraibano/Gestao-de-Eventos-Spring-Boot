package com.ingressos.api.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ingressos.api.dto.GeneralDto;
import com.ingressos.api.exceptions.IsNullException;
import com.ingressos.api.model.OrderModel;
import com.ingressos.api.repository.OrderRepository;
import com.ingressos.api.util.InternationalizationUtil;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class OrderService {

    @Autowired
    private OrderRepository repository;
    @Autowired
    private InternationalizationUtil messageInternationalization;

    @Transactional(readOnly = true)
    public List<OrderModel> findAll() {
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<OrderModel> findById(GeneralDto dto) {
        if (dto == null || dto.id() == null) {
            throw new IsNullException(messageInternationalization.getMessage("id.null"));
        }
        return repository.findById(dto.id());
    }

    @Transactional(readOnly = true)
    public List<OrderModel> findByUser(GeneralDto dto) {
        if (dto == null || dto.id() == null) {
            throw new IsNullException(messageInternationalization.getMessage("id.null"));
        }
        return repository.findByUser_IdOrderByCreatedAtDesc(dto.id().longValue());
    }

    @Transactional(readOnly = true)
    public List<OrderModel> findByEvent(GeneralDto dto) {
        if (dto == null || dto.id() == null) {
            throw new IsNullException(messageInternationalization.getMessage("id.null"));
        }
        return repository.findByEvent_IdOrderByCreatedAtDesc(dto.id().longValue());
    }

    @Transactional(readOnly = true)
    public List<OrderModel> findByTicket(GeneralDto dto) {
        if (dto == null || dto.id() == null) {
            throw new IsNullException(messageInternationalization.getMessage("id.null"));
        }
        return repository.findByTicket_IdOrderByCreatedAtDesc(dto.id().longValue());
    }

    @Transactional
    public OrderModel save(OrderModel order) {
        if (order == null) {
            throw new IsNullException(messageInternationalization.getMessage("order.null"));
        }
        return repository.save(order);
    }
}
