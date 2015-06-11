package com.indra.sishe.entity;

import java.io.Serializable;
import java.util.Date;

import com.indra.sishe.enums.StatusEnum;

public class Folga implements Serializable {

	private static final long serialVersionUID = 1192553411222129954L;
	
	private Long id;
	
	private Usuario solicitante;
	
	private Usuario Aprovador;
	
	private Date dataSolicitacao;
	
	private Date dataFolga;
	
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

	public Usuario getAprovador() {
		return Aprovador;
	}

	public void setAprovador(Usuario aprovador) {
		Aprovador = aprovador;
	}

	public Date getDataSolicitacao() {
		return dataSolicitacao;
	}

	public void setDataSolicitacao(Date dataSolicitacao) {
		this.dataSolicitacao = dataSolicitacao;
	}

	public Date getDataFolga() {
		return dataFolga;
	}

	public void setDataFolga(Date dataFolga) {
		this.dataFolga = dataFolga;
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

	
}
