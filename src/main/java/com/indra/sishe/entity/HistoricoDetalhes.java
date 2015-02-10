package com.indra.sishe.entity;

import java.io.Serializable;

public class HistoricoDetalhes implements Serializable{

	private static final long serialVersionUID = 3339476117649747379L;

	private Long id;
	
	private Historico historico;
	
	private Integer minutos;
	
	private Integer porcentagem;
	
	private Integer valor;

	public HistoricoDetalhes() {
	}

	public HistoricoDetalhes(Integer minutos, Integer porcentagem, Integer valor, Historico historico) {
		this.minutos = minutos;
		this.porcentagem = porcentagem;
		this.valor = valor;
		this.historico = historico;
	}
	
	public HistoricoDetalhes(Integer minutos, Integer valor, Historico historico) {
		this.minutos = minutos;
		this.valor = valor;
		this.historico = historico;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Historico getHistorico() {
		return historico;
	}

	public void setHistorico(Historico historico) {
		this.historico = historico;
	}

	public Integer getMinutos() {
		return minutos;
	}

	public void setMinutos(Integer minutos) {
		this.minutos = minutos;
	}

	public Integer getPorcentagem() {
		return porcentagem;
	}

	public void setPorcentagem(Integer porcentagem) {
		this.porcentagem = porcentagem;
	}

	public Integer getValor() {
		return valor;
	}

	public void setValor(Integer valor) {
		this.valor = valor;
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
		HistoricoDetalhes other = (HistoricoDetalhes) obj;
		if (id == null) {
			if (other.id != null) return false;
		} else if (!id.equals(other.id)) return false;
		return true;
	}
	
}
