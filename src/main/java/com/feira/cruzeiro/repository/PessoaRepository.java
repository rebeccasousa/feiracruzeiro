package com.feira.cruzeiro.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.feira.cruzeiro.model.Pessoa;

public interface PessoaRepository extends JpaRepository<Pessoa, Long> {
	
	@Query("SELECT p FROM Pessoa p JOIN p.user u where u.username =:username")
	Pessoa findByUser(String username);
	
}
