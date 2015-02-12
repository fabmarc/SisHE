package com.indra.sishe.enums;

public enum Mes {

	Janeiro("01"),
	Fevereiro("02"),
	Março("03"),
	Abril("04"),
	Maio("05"),
	Junho("06"),
	Julho("07"),
	Agosto("08"),
	Setembro("09"),
	Outubro("10"),
	Novembro("11"),
	Dezembro("12");
	
	private final String numero;

	Mes(String numero) {
		this.numero = numero;
	}

	public String getNumero() {
		return numero;
	}

	public static Mes obterMes(String numeroMes) {
		for (Mes mes : Mes.values()) {
			if (numeroMes == mes.getNumero()) return mes;
		}
		return null;
	}

}
