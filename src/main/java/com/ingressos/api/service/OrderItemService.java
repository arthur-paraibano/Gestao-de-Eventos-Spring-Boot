package com.ingressos.api.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ingressos.api.dto.GeneralDto;
import com.ingressos.api.exceptions.IncorrectParameterException;
import com.ingressos.api.exceptions.IsNullException;
import com.ingressos.api.model.OrderModel;
import com.ingressos.api.repository.OrderRepository;
import com.ingressos.api.util.InternationalizationUtil;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class OrderItemService {
    @Autowired
    private OrderRepository repository;
    @Autowired
    private InternationalizationUtil messageIniernat;

    @Transactional(readOnly = true)
    public List<OrderModel> findAll() {
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public OrderModel findById(GeneralDto dto) {
        if (dto == null || dto.id() == null) {
            throw new IsNullException(messageIniernat.getMessage("id.null"));
        }
        return repository.findById(dto.id())
                .orElseThrow(() -> new IncorrectParameterException(
                        messageIniernat.getMessage("descriptions.order.notfound")));
    }
}
