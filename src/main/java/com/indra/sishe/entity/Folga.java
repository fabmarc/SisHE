package com.indra.sishe.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.primefaces.model.DefaultScheduleEvent;
import org.primefaces.model.ScheduleEvent;

import com.indra.sishe.enums.StatusEnum;

public class Folga implements Serializable {

	private static final long serialVersionUID = 1192553411222129954L;

	private Long id;

	private String titulo;
	
	private Usuario solicitante;

	private Usuario lider;
	
	private Usuario gerente;
	
	private Date dataSolicitacao;
	
	private Date dataAprovacaoLider;
	
	private Date dataAprovacaoGerente;

	private StatusEnum StatusLider;
	
	private StatusEnum StatusGerente;

	private Date dataInicio;
	
	private Date dataFim;

	private List<DatasFolga> datasFolga;

	private String observacao;

	
	public Folga(){
		datasFolga = new ArrayList<DatasFolga>();
	}
	
	public StatusEnum getStatusGeral(){
		if (this.StatusLider == StatusEnum.Reprovada) {
			return StatusEnum.Reprovada;
		}else {
			return StatusGerente;
		}
	}

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

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public Date getDataInicio() {
		return dataInicio;
	}

	public void setDataInicio(Date dataInicio) {
		this.dataInicio = dataInicio;
	}

	public Date getDataFim() {
		return dataFim;
	}

	public void setDataFim(Date dataFim) {
		this.dataFim = dataFim;
	}

	public Usuario getLider() {
		return lider;
	}

	public void setLider(Usuario lider) {
		this.lider = lider;
	}

	public Usuario getGerente() {
		return gerente;
	}

	public void setGerente(Usuario gerente) {
		this.gerente = gerente;
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
		return StatusLider;
	}

	public void setStatusLider(StatusEnum statusLider) {
		StatusLider = statusLider;
	}

	public StatusEnum getStatusGerente() {
		return StatusGerente;
	}

	public void setStatusGerente(StatusEnum statusGerente) {
		StatusGerente = statusGerente;
	}

}
