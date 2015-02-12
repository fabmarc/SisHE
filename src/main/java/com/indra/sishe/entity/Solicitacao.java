package com.indra.sishe.entity;

import java.io.Serializable;
import java.util.Date;

public class Solicitacao implements Serializable{

	private static final long serialVersionUID = 3212545696646807422L;

	private Long id;
	
	private Sistema sistema = new Sistema();
	
	private Usuario usuario;
	
	private Usuario lider;
	
	private Status statusLider;
	
	private Usuario gerente;
	
	private Status statusGerente;
	
	private Date data;

	private Date dataAprovacaoLider;
	
	private Date dataAprovacaoGerente;
	
	private Date horaInicio;
	
	private Date horaFinal;	
	
	private String descricao;

	public Solicitacao() {
	}
	
	public Solicitacao(Long id) {
		this.id = id;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Sistema getSistema() {
		return sistema;
	}

	public void setSistema(Sistema sistema) {
		this.sistema = sistema;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public String getStatus() {

		if (statusLider.getId() != 0 && statusGerente.getId() == 0) {
			if (statusLider.getId() == 1) {
				return "Aguardando Gerente";	
			}else if (statusLider.getId() == 2) {
				return "Reprovado pelo Líder";
			}
		}else if(statusGerente.getId() != null){
			if (statusGerente.getId() == 1) {
				return "Concluido";	
			}else if (statusGerente.getId() == 2) {
				return "Reprovado pelo Gerente";
			}
		}
		return "Aguardando Líder";
	}

	public Usuario getLider() {
		return lider;
	}

	public void setLider(Usuario lider) {
		this.lider = lider;
	}

	public Status getStatusLider() {
		return statusLider;
	}

	public void setStatusLider(Status statusLider) {
		this.statusLider = statusLider;
	}

	public Usuario getGerente() {
		return gerente;
	}

	public void setGerente(Usuario gerente) {
		this.gerente = gerente;
	}

	public Status getStatusGerente() {
		return statusGerente;
	}

	public void setStatusGerente(Status statusGerente) {
		this.statusGerente = statusGerente;
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public Date getDataAprovacaoLider() {
		return dataAprovacaoLider;
	}

	public void setDataAprovacaoLider(Date dataAprovacaoLider) {
		this.dataAprovacaoLider = dataAprovacaoLider;
	}

	public Date getDataAprovacaoGerente() {
		return dataAprovacaoGerente;
	}

	public void setDataAprovacaoGerente(Date dataAprovacaoGerente) {
		this.dataAprovacaoGerente = dataAprovacaoGerente;
	}

	public Date getHoraInicio() {
		return horaInicio;
	}

	public void setHoraInicio(Date horaInicio) {
		this.horaInicio = horaInicio;
	}

	public Date getHoraFinal() {
		return horaFinal;
	}

	public void setHoraFinal(Date horaFinal) {
		this.horaFinal = horaFinal;
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
		Solicitacao other = (Solicitacao) obj;
		if (id == null) {
			if (other.id != null) return false;
		} else if (!id.equals(other.id)) return false;
		return true;
	}
	
}
