package com.ingressos.api.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ingressos.api.dto.GeneralDto;
import com.ingressos.api.exceptions.IncorrectParameterException;
import com.ingressos.api.exceptions.IsNullException;
import com.ingressos.api.model.UserModel;
import com.ingressos.api.repository.UserRepository;
import com.ingressos.api.util.InternationalizationUtil;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UserService {
    @Autowired
    private UserRepository repository;
    @Autowired
    private InternationalizationUtil messageIniernat;

    @Transactional(readOnly = true)
    public List<UserModel> findAll() {
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public UserModel findById(GeneralDto dto) {
        if (dto == null || dto.id() == null) {
            throw new IsNullException(messageIniernat.getMessage("id.null"));
        }
        return repository.findById(dto.id())
                .orElseThrow(() -> new IncorrectParameterException(
                        messageIniernat.getMessage("descriptions.tickettype.notfound")));
    }
}
