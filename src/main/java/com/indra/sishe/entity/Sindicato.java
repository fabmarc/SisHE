package com.indra.sishe.entity;

import java.io.Serializable;

public class Sindicato implements Serializable, Comparable<Sindicato> {

	private static final long serialVersionUID = 5891090718221141954L;

	private Long id;

	private Estado estado;

	private String descricao;

	public Sindicato() {

	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Estado getEstado() {
		return estado;
	}

	public void setEstado(Estado estado) {
		this.estado = estado;
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
		result = prime * result + ((estado == null) ? 0 : estado.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (!(obj instanceof Sindicato)) return false;
		Sindicato other = (Sindicato) obj;

		if (estado == null) {
			if (other.estado != null) return false;
		} else if (!estado.equals(other.estado)) { return false; }

		if (id == null) {
			if (other.id != null) return false;
		} else if (!id.equals(other.id)) return false;
		return true;
	}

	@Override
	public int compareTo(Sindicato o) {
		int valor = descricao.toLowerCase().compareTo(o.descricao.toLowerCase());
		if (valor != 0) {
			return valor;
		} else {
			return 1;
		}
	}
}
