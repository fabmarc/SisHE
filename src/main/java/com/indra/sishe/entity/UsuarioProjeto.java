package com.indra.sishe.entity;

import java.io.Serializable;
import java.lang.reflect.Constructor;

public class UsuarioProjeto implements Serializable, Comparable<UsuarioProjeto> {

	private static final long serialVersionUID = -1700372408472174319L;
	private Long id;
	private Usuario usuario;
	private Projeto projeto;

	public UsuarioProjeto(Long id, Usuario usuario, Projeto projeto) {
		super();
		this.id = id;
		this.usuario = usuario;
		this.projeto = projeto;
	}

	public UsuarioProjeto() {

	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((projeto == null) ? 0 : projeto.hashCode());
		result = prime * result + ((usuario == null) ? 0 : usuario.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		UsuarioProjeto other = (UsuarioProjeto) obj;
		if (id == null) {
			if (other.id != null) return false;
		} else if (!id.equals(other.id)) return false;
		if (projeto == null) {
			if (other.projeto != null) return false;
		} else if (!projeto.equals(other.projeto)) return false;
		if (usuario == null) {
			if (other.usuario != null) return false;
		} else if (!usuario.equals(other.usuario)) return false;
		return true;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public Projeto getProjeto() {
		return projeto;
	}

	public void setProjeto(Projeto projeto) {
		this.projeto = projeto;
	}

	@Override
	public int compareTo(UsuarioProjeto o) {
		int valor = usuario.getNome().toLowerCase().compareTo(o.getUsuario().getNome().toLowerCase());
		if (valor != 0) {
			return valor;
		} else {
			return 1;
		}

	}

}
