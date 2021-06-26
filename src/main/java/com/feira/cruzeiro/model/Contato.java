package com.feira.cruzeiro.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "CONTATO")
public class Contato {


	public Contato() {
		
	}

	public Contato(@NotBlank String numero, @NotBlank String email, @NotBlank int telefone) {
		super();
		this.numero = numero;
		this.email = email;
//		this.telefone = telefone;
	}

	public Contato(String complemento, @NotBlank String numero, @NotBlank String email, @NotBlank int telefone) {
		super();
		this.complemento = complemento;
		this.numero = numero;
		this.email = email;
//		this.telefone = telefone;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	private String complemento;

	@NotBlank
	private String numero;
	
	@NotBlank
	private String email;
	
	private Long telefone;
	

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getComplemento() {
		return complemento;
	}

	public void setComplemento(String complemento) {
		this.complemento = complemento;
	}

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Long getTelefone() {
		return telefone;
	}

	public void setTelefone(Long telefone) {
		this.telefone = telefone;
	}
	
}
