package com.feira.cruzeiro.model;

public enum TipoLoja {

	LOJA("LOJA"), 
	RESTAURANTE("RESTAURANTE"), 
	SERVICO("SERVICO");
	
	private String nome;

	TipoLoja(String name) {
		this.nome = name;
	}
	
	TipoLoja() {
	}

	public static String getNome(String nome) {
		return nome.equals("LOJA") ? "LOJA" : nome.equals("RESTAURANTE") ? "RESTAURANTE" : "SERVICO";
	}
	
}
