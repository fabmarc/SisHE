package com.indra.sishe.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "cliente")
public class Cliente implements Serializable, Comparable<Cliente> {

	private static final long serialVersionUID = 596604411492271290L;

	@Id
//	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sq_cliente")
//	@SequenceGenerator(name = "sq_cliente", schema = "public", sequenceName = "sq_cliente", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_cliente", nullable = false)
	private Long idCliente;

	@Column(name = "nome_cliente")
	private String nomeCliente;

	public Cliente() {

	}

	public Cliente(Long id, String nome) {
		this.idCliente = id;
		this.nomeCliente = nome;
	}

	public Long getIdCliente() {
		return idCliente;
	}

	public void setIdCliente(Long idCliente) {
		this.idCliente = idCliente;
	}

	public String getNomeCliente() {
		return nomeCliente;
	}

	public void setNomeCliente(String nomeCliente) {
		this.nomeCliente = nomeCliente;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((idCliente == null) ? 0 : idCliente.hashCode());
		result = prime * result
				+ ((nomeCliente == null) ? 0 : nomeCliente.hashCode());
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
		Cliente other = (Cliente) obj;
		if (idCliente == null) {
			if (other.idCliente != null)
				return false;
		} else if (!idCliente.equals(other.idCliente))
			return false;
		if (nomeCliente == null) {
			if (other.nomeCliente != null)
				return false;
		} else if (!nomeCliente.equals(other.nomeCliente))
			return false;
		return true;
	}

	@Override
	public int compareTo(Cliente o) {
		int valor = nomeCliente.toLowerCase().compareTo(
				o.nomeCliente.toLowerCase());
		if (valor != 0) {
			return valor;
		} else {
			return 1;
		}
	}

}
