package com.indra.sishe.enums;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public enum MesEnum {

	Janeiro("01", 1), Fevereiro("02", 2), Marco("03", 3), Abril("04", 4), Maio("05", 5), Junho("06", 6), Julho(
			"07", 7), Agosto("08", 8), Setembro("09", 9), Outubro("10", 10), Novembro("11", 11), Dezembro("12", 12);

	private final String numero;
	private final Integer valor;
	private Integer ano;

	MesEnum(String numero, Integer valor) {
		this.numero = numero;
		this.valor = valor;
	}

	public String getNumero() {
		return numero;
	}

	public Integer getValor() {
		return valor;
	}

	public Integer getAno() {
		return ano;
	}

	public void setAno(Integer ano) {
		this.ano = ano;
	}

	public static MesEnum obterMes(String numeroMes) {
		for (MesEnum mes : MesEnum.values()) {
			if (numeroMes == mes.getNumero()) return mes;
		}
		return null;
	}

	public static MesEnum obterMes(Integer valorMes) {
		for (MesEnum mes : MesEnum.values()) {
			if (valorMes == mes.getValor()) return mes;
		}
		return null;
	}
	
	public static List<MesEnum> listaMeses() {
		List<MesEnum> listaMeses = new ArrayList<MesEnum>(Arrays.asList(MesEnum.values()));
		return listaMeses;
	}
	
	public static List<Integer> anosAtuais(){
		List<Integer> anos = new ArrayList<Integer>();
		Calendar cal = Calendar.getInstance();
		anos.add(cal.get(Calendar.YEAR)-1);
		anos.add(cal.get(Calendar.YEAR));
		anos.add(cal.get(Calendar.YEAR) + 1);
		return anos;
	}

	public static List<MesEnum> seisMeses() {
		List<MesEnum> meses = new ArrayList<MesEnum>();
		Calendar cal = Calendar.getInstance();
		Integer mesAtual = cal.get(Calendar.MONTH) + 1;
		Integer anoAtual = cal.get(Calendar.YEAR);
		Integer temp = 0;
		Integer temp2 = mesAtual;
		for (int i = 0; i < 5; i++) {
			temp2 = temp2 - 1;
			if (temp2 < 1) {
				temp = temp + 1;
			}
		}
		if (temp2 < 1) {
			temp = 12 - (temp - 1);
		} else {
			temp = temp2;
		}
		temp2 = 1;

		for (int i = 0; i < 6; i++) {
			if (temp + i > 12) {
				MesEnum mesTemp = obterMes(temp2);
				mesTemp.setAno(anoAtual);
				meses.add(mesTemp);
				temp2++;
			} else {
				MesEnum mesTemp = obterMes(temp + i);
				mesTemp.setAno(anoAtual - 1);
				meses.add(mesTemp);
			}
		}
		return meses;
	}

}
