package com.indra.sishe.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

//@Entity
@Table(name = "cargo")
public class Cargo implements Serializable, Comparable<Cargo> {

	private static final long serialVersionUID = -4975465653456184465L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)
	private Long id;

	@Column(name = "nome", nullable = false)
	private String nome;

	public Cargo() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long idCargo) {
		this.id = idCargo;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nomeCargo) {
		this.nome = nomeCargo;
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
		Cargo other = (Cargo) obj;
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
	public int compareTo(Cargo o) {
		int valor = nome.toLowerCase().compareTo(o.nome.toLowerCase());
		if (valor != 0) {
			return valor;
		} else {
			return 1;
		}
	}

}
