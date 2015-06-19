package com.indra.sishe.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.indra.sishe.enums.StatusEnum;

public class Folga implements Serializable {

	private static final long serialVersionUID = 1192553411222129954L;

	private Long id;

	private Usuario solicitante;

	private Usuario gerente;
	
	private Usuario lider;

	private Date dataSolicitacao;

	private List<DatasFolga> datasFolga;

	private Date dataAprovacaoLider;
	
	private Date dataAprovacaoGerente;

	private String observacao;

	private StatusEnum statusLider;
	
	private StatusEnum statusGerente;

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

	public Date getDataSolicitacao() {
		return dataSolicitacao;
	}

	public void setDataSolicitacao(Date dataSolicitacao) {
		this.dataSolicitacao = dataSolicitacao;
	}

	public String getObservacao() {
		return observacao;
	}

	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}

	public List<DatasFolga> getDatasFolga() {
		return datasFolga;
	}

	public void setDatasFolga(List<DatasFolga> datasFolga) {
		this.datasFolga = datasFolga;
	}

	public Usuario getGerente() {
		return gerente;
	}

	public void setGerente(Usuario gerente) {
		this.gerente = gerente;
	}

	public Usuario getLider() {
		return lider;
	}

	public void setLider(Usuario lider) {
		this.lider = lider;
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

	public StatusEnum getStatusLider() {
		return statusLider;
	}

	public void setStatusLider(StatusEnum statusLider) {
		this.statusLider = statusLider;
	}

	public StatusEnum getStatusGerente() {
		return statusGerente;
	}

	public void setStatusGerente(StatusEnum statusGerente) {
		this.statusGerente = statusGerente;
	}

}
