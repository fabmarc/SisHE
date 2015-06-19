package com.indra.sishe.entity;

import java.io.Serializable;
import java.util.Date;

public class HistoricoFolga implements Serializable {
	
	private static final long serialVersionUID = 1224982052506978918L;

	private Long id;
	
	private Folga folga;
	
	private Usuario gerente;
	
	private Date data;
	
	private String observacao;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
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
