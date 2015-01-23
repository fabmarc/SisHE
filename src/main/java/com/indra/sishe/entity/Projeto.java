package com.indra.sishe.entity;

import java.io.Serializable;

public class Projeto implements Serializable, Comparable<Projeto> {

	private static final long serialVersionUID = 7037450049743453705L;

	private Long id;
	
	private Usuario gerente;
	
	private String nome;
	
	private String descricao;

	public Projeto(Usuario gerente) {
		this.gerente = gerente;
	}

	public Projeto() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Usuario getGerente() {
		return gerente;
	}

	public void setGerente(Usuario usuario) {
		this.gerente = usuario;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((nome == null) ? 0 : nome.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Projeto other = (Projeto) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (nome == null) {
			if (other.nome != null)
				return false;
		} else if (!nome.equals(other.nome))
			return false;
		return true;
	}

	@Override
	public int compareTo(Projeto o) {
		int valor = nome.toLowerCase().compareTo(o.nome.toLowerCase());
		if (valor != 0){
			return valor;
		}else {
			return 1;
		}
	}
	
	
	
	
}
