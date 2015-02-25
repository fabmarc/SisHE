package com.indra.sishe.entity;

import java.io.Serializable;
import java.util.Date;

public class Historico implements Serializable {

	private static final long serialVersionUID = -5060947608595810657L;

	private Long id;

	private Usuario gerente;

	private Solicitacao solicitacao;

	private BancoHoras bancoHoras;

	private Date data;

	private String descricao;

	public Historico() {
	}

	public Historico(Solicitacao solicitacao) {
		this.solicitacao = solicitacao;
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

	public void setGerente(Usuario gerente) {
		this.gerente = gerente;
	}

	public Solicitacao getSolicitacao() {
		return solicitacao;
	}

	public void setSolicitacao(Solicitacao solicitacao) {
		this.solicitacao = solicitacao;
	}

	public BancoHoras getBancoHoras() {
		return bancoHoras;
	}

	public void setBancoHoras(BancoHoras bancoHoras) {
		this.bancoHoras = bancoHoras;
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
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
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		Historico other = (Historico) obj;
		if (id == null) {
			if (other.id != null) return false;
		} else if (!id.equals(other.id)) return false;
		return true;
	}

}
