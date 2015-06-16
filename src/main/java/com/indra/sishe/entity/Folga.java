package com.indra.sishe.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.indra.sishe.enums.StatusEnum;

public class Folga implements Serializable {

	private static final long serialVersionUID = 1192553411222129954L;

	private Long id;

	private Usuario solicitante;

	private Usuario avaliador;

	private Date dataSolicitacao;

	private List<DatasFolga> datasFolga;

	private Date dataAprovacao;

	private String observacao;

	private StatusEnum status;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Usuario getSolicitante() {
		return solicitante;
	}

	public void setSolicitante(Usuario solicitante) {
		this.solicitante = solicitante;
	}

	public Usuario getAvaliador() {
		return avaliador;
	}

	public void setAvaliador(Usuario aprovador) {
		avaliador = aprovador;
	}

	public Date getDataSolicitacao() {
		return dataSolicitacao;
	}

	public void setDataSolicitacao(Date dataSolicitacao) {
		this.dataSolicitacao = dataSolicitacao;
	}

	public Date getDataAprovacao() {
		return dataAprovacao;
	}

	public void setDataAprovacao(Date dataAprovacao) {
		this.dataAprovacao = dataAprovacao;
	}

	public String getObservacao() {
		return observacao;
	}

	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}

	public StatusEnum getStatus() {
		return status;
	}

	public void setStatus(StatusEnum status) {
		this.status = status;
	}

	public List<DatasFolga> getDatasFolga() {
		return datasFolga;
	}

	public void setDatasFolga(List<DatasFolga> datasFolga) {
		this.datasFolga = datasFolga;
	}

}
