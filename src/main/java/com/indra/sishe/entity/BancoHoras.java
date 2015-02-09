package com.indra.sishe.entity;

import java.io.Serializable;

public class BancoHoras implements Serializable{
	
	private static final long serialVersionUID = -6168029499594993488L;

	private Long id;
	
	private Usuario usuario;
	
	private Long saldo;

	public BancoHoras() {
	}
	
	public BancoHoras(Usuario usuario, Long saldo) {
		this.usuario = usuario;
		this.saldo = saldo;
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

	public Long getSaldo() {
		return saldo;
	}

	public void setSaldo(Long saldo) {
		this.saldo = saldo;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		BancoHoras other = (BancoHoras) obj;
		if (id == null) {
			if (other.id != null) return false;
		} else if (!id.equals(other.id)) return false;
		return true;
	}
	
}
