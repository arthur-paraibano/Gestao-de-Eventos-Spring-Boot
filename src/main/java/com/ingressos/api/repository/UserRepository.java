package com.ingressos.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ingressos.api.model.UserModel;

@Repository
public interface UserRepository extends JpaRepository<UserModel, Integer> {

}
