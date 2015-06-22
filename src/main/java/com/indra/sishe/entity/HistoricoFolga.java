package com.indra.sishe.entity;

import java.io.Serializable;
import java.util.Date;

public class HistoricoFolga implements Serializable {
	
	private static final long serialVersionUID = 1224982052506978918L;

	private Integer id;
	
	private Folga folga;
	
	private Usuario gerente;
	
	private Date data;
	
	private String observacao;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Folga getFolga() {
		return folga;
	}

	public void setFolga(Folga folga) {
		this.folga = folga;
	}

	public Usuario getGerente() {
		return gerente;
	}

	public void setGerente(Usuario gerente) {
		this.gerente = gerente;
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public String getObservacao() {
		return observacao;
	}

	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}

}
