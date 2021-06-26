package com.feira.cruzeiro.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.feira.cruzeiro.model.Loja;

public interface LojaRepository extends JpaRepository<Loja, Long>{
	
	@Query("SELECT l FROM Loja l JOIN l.pessoa p where p.id =:id")
	public List<Loja> findByPessoa(@Param("id")Long id);
	
	@Query("SELECT l FROM Loja l WHERE l.tipo =:tipo")
	public List<Loja> findByTipo(@Param("tipo")String tipo);

}
