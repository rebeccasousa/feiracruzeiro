package com.feira.cruzeiro.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.feira.cruzeiro.model.Evento;

public interface EventoRepository extends JpaRepository<Evento, Long>{

	@Query("SELECT e FROM Evento e JOIN e.user u WHERE u.username =:nome")
	public List<Evento> findEventosByUser(String nome);
}
