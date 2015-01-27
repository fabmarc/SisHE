package com.indra.sishe.enums;

public enum DiaSemanaEnum {

	Domingo(1), Segunda(2), Terça(3), Quarta(4), Quinta(5), Sexta(6), Sábado(7);
	private final int numeroDia;

	DiaSemanaEnum(int numeroDia) {

		this.numeroDia = numeroDia;
	}

	public int numeroDia() {

		return this.numeroDia;
	}

	public static DiaSemanaEnum obterDiaSemana(int numeroDia) {

		for (DiaSemanaEnum dia : DiaSemanaEnum.values()) {
			if (numeroDia == dia.numeroDia()) return dia;
		}
		return null;
	}

}
