package com.indra.sishe.entity;

import java.io.Serializable;

public class Sistema implements Serializable, Comparable<Sistema> {

	private static final long serialVersionUID = 1L;
	private Long id;
	private Usuario lider;
	private String nome;
	private String descricao;
	private Projeto projeto;

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
		
		if (this == obj) return true;
		if (obj == null) return false;
		if (!(obj instanceof Sistema)) return false;
		Sistema other = (Sistema) obj;
		if (id == null) {
			if (other.id != null) return false;
		} else if (!id.equals(other.id)) return false;
		if (nome == null) {
			if (other.nome != null) return false;
		} else if (!nome.equals(other.nome)) return false;
		return true;
	}

	public Sistema() {

	}

	public Sistema(Long id, Usuario usuario, String nome, String descricao) {
		
		super();
		this.id = id;
		this.lider = usuario;
		this.nome = nome;
		this.descricao = descricao;
	}

	@Override
	public int compareTo(Sistema o) {		
		
		int valor = nome.toLowerCase().compareTo(o.getNome().toLowerCase());
		if (valor != 0) {
			return valor;
		} else {
			return 0;
		}
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Usuario getLider() {
		return lider;
	}

	public void setLider(Usuario lider) {
		this.lider = lider;
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

	public Projeto getProjeto() {
		return projeto;
	}

	public void setProjeto(Projeto projeto) {
		this.projeto = projeto;
	}

}
