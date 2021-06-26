package com.feira.cruzeiro.utils;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.feira.cruzeiro.model.Contato;
import com.feira.cruzeiro.model.Loja;
import com.feira.cruzeiro.model.Pessoa;
import com.feira.cruzeiro.model.TipoLoja;
import com.feira.cruzeiro.repository.ContatoRepository;
import com.feira.cruzeiro.repository.LojaRepository;
import com.feira.cruzeiro.repository.PessoaRepository;

@Component
public class DummyData {

	@Autowired
	private LojaRepository lojarepository;
	
	@Autowired
	private PessoaRepository pessoaRepository;
	
	@Autowired
	private ContatoRepository contatoRepository;
	
//	@PostConstruct
	public void init() {
	
		List<Loja> lojas = new ArrayList<>();
		
		Pessoa p1 = new Pessoa("Larissa da Silva", 'F', 23);
		Contato c1 = new Contato("10A", "pelicularrissa@gmail.com", 983550321);
		pessoaRepository.save(p1);
		contatoRepository.save(c1);
		
		Pessoa p2 = new Pessoa("Sheila Melo", 'F', 27);
		Contato c2 = new Contato("12B", "loja@hotmail.com", 888732632);
		pessoaRepository.save(p2);
		contatoRepository.save(c2);
		
		Loja loja = new Loja();
		loja.setNome("Pelicularissa");
		loja.setDelivery(true);
//		loja.setTipo(TipoLoja.LOJA);
		loja.setResumo("Loja de películas e acessorios para celulares, dos mais diversos estilos.");
		loja.setFormaPagamento("Cartão de crédito, cartão e crédito");
		loja.setContato(c1);
		loja.setPessoa(p1);
		
		Loja loja1 = new Loja();
		loja1.setNome("Sobransheila");
		loja1.setDelivery(true);
//		loja1.setTipo(TipoLoja.SERVICO);
		loja1.setResumo("Serviços para realçar a beleza.");
		loja1.setFormaPagamento("Cartão de crédito, cartão e crédito");
		loja1.setContato(c2);
		loja1.setPessoa(p2);
		
		lojas.add(loja1);
		lojas.add(loja);
		
		lojarepository.saveAll(lojas);
		System.out.println(lojas);
	}
	
}
