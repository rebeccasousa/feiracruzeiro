package com.feira.cruzeiro.repository.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.feira.cruzeiro.model.Contato;
import com.feira.cruzeiro.model.Loja;


public class LojaRepositoryImpl {
	
	@PersistenceContext
	private EntityManager entityManager;
	
	public List<Loja> getLojasByTipo(String tipo){
		
		String hql =	" SELECT l " +
						" FROM Loja l " +
						" WHERE l.tipo = :tipo ";

		Query q = entityManager.createQuery(hql);
		q.setParameter("tipo", tipo);
		
		List<Loja> lojas = (List<Loja>) q.getResultList();
		
		return lojas;
	}
	
	public List<Loja> getLojasByTipoLimit(String tipo, int limit){
		
		String sql =	" SELECT l.id, l.nome, l.resumo, l.arquivo " +
				" FROM loja l " +
				" WHERE l.tipo = :tipo LIMIT :limit";

		Query q = entityManager.createNativeQuery(sql);
		q.setParameter("tipo", tipo);
		q.setParameter("limit", limit);
		
		List<Object[]> list = q.getResultList();
		List<Loja> lojas= new ArrayList<>();
		
		for(Object[] obj : list) {
			int i = 0;
			Loja l = new Loja();
			l.setId(Long.parseLong(String.valueOf(obj[i++])));
			l.setNome((String) obj[i++]);
			l.setResumo((String) obj[i++]);
			l.setArquivo((String) obj[i++]);
			
			lojas.add(l);
		}
		
		
		return lojas;
	}
	
	public List<Loja> getLojasByParam(String param){
		
		String sql =	" SELECT l.id, l.nome, l.resumo, l.tipo, l.forma_pagamento, l.arquivo, " +
						" l.delivery, ctt.id as idCtt, ctt.complemento, ctt.numero, ctt.email, ctt.telefone " +
						" FROM loja l " +
						" JOIN contato ctt ON ctt.id = l.id_contato" +
						" WHERE l.resumo LIKE :param";

		Query q = entityManager.createNativeQuery(sql);
		q.setParameter("param", "%" + param + "%");
		List<Object[]> list = q.getResultList();
		List<Loja> lojas= new ArrayList<>();
		
		for(Object[] obj : list) {
			int i = 0;
			Loja l = new Loja();
			l.setId(Long.parseLong(String.valueOf(obj[i++])));
			l.setNome((String) obj[i++]);
			l.setResumo((String) obj[i++]);
			l.setTipo((String) obj[i++]);
			l.setFormaPagamento((String) obj[i++]);
			l.setArquivo((String) obj[i++]);
			l.setDelivery((boolean) obj[i++]);
			
			Contato c = new Contato();
			c.setId(Long.parseLong(String.valueOf(obj[i++])));
			c.setComplemento((String) obj[i++]);
			c.setNumero((String) obj[i++]);
			c.setEmail((String) obj[i++]);
			c.setTelefone(Long.parseLong(String.valueOf(obj[i++])));
			l.setContato(c);
			lojas.add(l);
		}
		
		return lojas;
	}

}
