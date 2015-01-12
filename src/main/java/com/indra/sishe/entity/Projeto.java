package com.indra.sishe.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name = "projeto")
public class Projeto implements Serializable {

	private static final long serialVersionUID = -3116324357564718848L;
	
	@Id
//	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sq_projeto")
//	@SequenceGenerator(name = "sq_projeto", schema = "public", sequenceName = "sq_projeto", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_projeto", nullable = false)
	private Integer idProjeto;
	
	
	@Column(name = "descricao_projeto", nullable = false)
	private String descricaoProjeto;
	
	
	
	public Integer getIdProjeto() {
		return idProjeto;
	}


	public void setIdProjeto(Integer idProjeto) {
		this.idProjeto = idProjeto;
	}


	public String getDescricaoProjeto() {
		return descricaoProjeto;
	}


	public void setDescricaoProjeto(String descricaoProjeto) {
		this.descricaoProjeto = descricaoProjeto;
	}



	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((idProjeto == null) ? 0 : idProjeto.hashCode());
		return result;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Projeto other = (Projeto) obj;
		if (idProjeto == null) {
			if (other.idProjeto != null)
				return false;
		} else if (!idProjeto.equals(other.idProjeto))
			return false;
		return true;
	}
	
	

}
