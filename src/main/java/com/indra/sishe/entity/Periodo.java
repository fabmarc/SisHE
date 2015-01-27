package com.indra.sishe.entity;

import java.io.Serializable;
import java.util.Date;

import com.indra.sishe.enums.DiaSemanaEnum;

public class Periodo implements Serializable, Comparable<Periodo> {

	private static final long serialVersionUID = 4911632352090360894L;

	private Long id;

	private Regra regra;

	private DiaSemanaEnum diaSemana;

	private Date horaInicio;

	private Date horaFim;

	private Integer porcentagem;

	public Periodo() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Regra getRegra() {
		return regra;
	}

	public void setRegra(Regra regra) {
		this.regra = regra;
	}

	public DiaSemanaEnum getDiaSemana() {
		return diaSemana;
	}

	public void setDiaSemana(DiaSemanaEnum diaSemana) {
		this.diaSemana = diaSemana;
	}

	public Date getHoraInicio() {
		return horaInicio;
	}

	public void setHoraInicio(Date horaInicio) {
		this.horaInicio = horaInicio;
	}

	public Date getHoraFim() {
		return horaFim;
	}

	public void setHoraFim(Date horaFim) {
		this.horaFim = horaFim;
	}

	public Integer getPorcentagem() {
		return porcentagem;
	}

	public void setPorcentagem(Integer porcentagem) {
		this.porcentagem = porcentagem;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((diaSemana == null) ? 0 : diaSemana.hashCode());
		result = prime * result + ((horaFim == null) ? 0 : horaFim.hashCode());
		result = prime * result + ((horaInicio == null) ? 0 : horaInicio.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {

		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		Periodo other = (Periodo) obj;
		if (diaSemana == null) {
			if (other.diaSemana != null) return false;
		} else if (!diaSemana.equals(other.diaSemana)) return false;
		if (horaFim == null) {
			if (other.horaFim != null) return false;
		} else if (!horaFim.equals(other.horaFim)) return false;
		if (horaInicio == null) {
			if (other.horaInicio != null) return false;
		} else if (!horaInicio.equals(other.horaInicio)) return false;
		if (id == null) {
			if (other.id != null) return false;
		} else if (!id.equals(other.id)) return false;
		return true;
	}

	@Override
	public int compareTo(Periodo o) {
		int valor = diaSemana.compareTo(o.diaSemana);
		if (valor != 0) {
			return valor;
		} else {
			return 1;
		}
	}

}
