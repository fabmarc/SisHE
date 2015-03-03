package com.indra.sishe.entity;

import java.io.Serializable;
import java.util.Date;

public class HistoricoDebito implements Serializable{

	private static final long serialVersionUID = -7281134492971892879L;
	
	private Long id;
	
	private Date data;
	
	private Integer minutos;
	
	private Usuario gerente;
	
	private BancoHoras banco;

	public HistoricoDebito(){
		
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public Integer getMinutos() {
		return minutos;
	}

	public void setMinutos(Integer minutos) {
		this.minutos = minutos;
	}

	public Usuario getGerente() {
		return gerente;
	}

	public void setGerente(Usuario gerente) {
		this.gerente = gerente;
	}

	public BancoHoras getBanco() {
		return banco;
	}

	public void setBanco(BancoHoras banco) {
		this.banco = banco;
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
		HistoricoDebito other = (HistoricoDebito) obj;
		if (id == null) {
			if (other.id != null) return false;
		} else if (!id.equals(other.id)) return false;
		return true;
	}
	
}
