package com.indra.sishe.entity;

import java.io.Serializable;
import java.util.Date;


public class Feriado implements Serializable, Comparable<Feriado>{

	private static final long serialVersionUID = 3382942206529054226L;

	private Long id;
	
	private Date data;

	private Character tipo;
	
	private String nome;	
	
	private String abrangencia;
	
	private Estado estado;
	
	private Cidade cidade;
	
	public Feriado(Estado estado, Cidade cidade) {
		this.estado = estado;
		this.cidade = cidade;
	}
	
	public Feriado() {
	}
	
	public Estado getEstado() {
		return estado;
	}

	public void setEstado(Estado estado) {
		this.estado = estado;
	}

	public Cidade getCidade() {
		return cidade;
	}

	public void setCidade(Cidade cidade) {
		this.cidade = cidade;
	}

	public String getAbrangencia() {
		return abrangencia;
	}

	public void setAbrangencia(String abrangencia) {
		this.abrangencia = abrangencia;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long idFeriado) {
		this.id = idFeriado;
	}
		
	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public Character getTipo() {
		return tipo;
	}
	
	public String getTipoFormatado() {
		if (getTipo().equals('F')){
			return "Fixo";
		}else{
			return "Móvel";
		}
	}

	public void setTipo(Character tipo) {
		this.tipo = tipo;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((data == null) ? 0 : data.hashCode());
		result = prime * result + ((tipo == null) ? 0 : tipo.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		Feriado other = (Feriado) obj;
		if (data == null) {
			if (other.data != null)
				return false;
		} else if (!data.equals(other.data))
			return false;
		if (tipo == null) {
			if (other.tipo != null)
				return false;
		} else if (!tipo.equals(other.tipo))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public int compareTo(Feriado o) {
		// TODO Auto-generated method stub
		return 0;
	}
	
	

}
