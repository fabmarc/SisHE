package com.indra.sishe.entity;

import java.io.Serializable;
import java.util.Date;

public class Regra implements Serializable, Comparable<Regra> {

	private static final long serialVersionUID = -7754813837217228348L;

	private Long id;
	
	private Sindicato sindicato;
	
	private String descricao;
	
	private Date dataInicio;
	
	private Date dataFim;
	
	private Double porcentagem;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Sindicato getSindicato() {
		return sindicato;
	}

	public void setSindicato(Sindicato sindicato) {
		this.sindicato = sindicato;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
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

	public Double getPorcentagem() {
		return porcentagem;
	}

	public void setPorcentagem(Double porcentagem) {
		this.porcentagem = porcentagem;
	}

	@Override
	public int compareTo(Regra o) {
		// TODO Auto-generated method stub
		return 0;
	}
	
	
}
