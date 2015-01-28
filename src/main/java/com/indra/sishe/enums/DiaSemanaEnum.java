package com.indra.sishe.enums;

public enum DiaSemanaEnum {

	Domingo(1, "Domingo"), Segunda(2, "Segunda-feira"), Terca(3, "Terça-feira"), Quarta(4, "Quarta-feira"), Quinta(5, "Quinta-feira"), Sexta(6, "Sexta-feira"), Sabado(7, "Sábado");
	private final int numeroDia;
	private final String nomeDia;

	DiaSemanaEnum(int numeroDia, String nomeDia) {

		this.numeroDia = numeroDia;
		this.nomeDia = nomeDia;
	}

	public int numeroDia() {

		return this.numeroDia;
	}
	
	public String nomeDia(){
		return this.nomeDia;
	}

	public static DiaSemanaEnum obterDiaSemana(int numeroDia) {

		for (DiaSemanaEnum dia : DiaSemanaEnum.values()) {
			if (numeroDia == dia.numeroDia()) return dia;
		}
		return null;
	}

}
