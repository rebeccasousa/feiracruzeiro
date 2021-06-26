package com.feira.cruzeiro.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.feira.cruzeiro.model.User;

public interface UserRepository extends JpaRepository<User, String> {

}
