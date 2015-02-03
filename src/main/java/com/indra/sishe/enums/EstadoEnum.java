package com.indra.sishe.enums;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum EstadoEnum {

	BR("BR", "Nacional", 0L), AC("AC", "Acre", 1L), AL("AL", "Alagoas", 2L), AP("AP", "Amap�", 3L), AM("AM",
			"Amazonas", 4L), BA("BA", "Bahia", 5L), CE("CE", "Cear�", 6L), DF("DF", "Distrito Federal", 7L), ES(
			"ES", "Esp�rito Santo", 8L), GO("GO", "Goi�s", 9L), MA("MA", "Maranh�o", 10L), MT("MT", "Mato Grosso",
			11L), MS("MS", "Mato Grosso do Sul", 12L), MG("MG", "Minas Gerais", 13L), PA("PA", "Par�", 14L), PB(
			"PB", "Para�ba", 15L), PR("PR", "Paran�", 16L), PE("PE", "Pernambuco", 17L), PI("PI", "Piau�", 18L), RJ(
			"RJ", "Rio de Janeiro", 19L), RN("RN", "Rio Grande do Norte", 20L), RS("RS", "Rio Grande do Sul", 21L), RO(
			"RO", "Rond�nia", 22L), RR("RR", "Roraima", 23L), SC("SC", "Santa Catarina", 24L), SP("SP",
			"S�o Paulo", 25L), SE("SE", "Sergipe", 26L), TO("TO", "Tocantins", 27L);

	private final String sigla;
	private final String nome;
	private final Long id;

	private EstadoEnum(String sigla, String nome, Long id) {
		this.sigla = sigla;
		this.nome = nome;
		this.id = id;
	}

	public static EstadoEnum obterEstado(Long id) {
		for (EstadoEnum estado : EstadoEnum.values()) {
			if (id == estado.id) return estado;
		}
		return null;
	}
	
	public static List<EstadoEnum> listaEstados() {
		List<EstadoEnum> listaEstados = new ArrayList<EstadoEnum>(Arrays.asList(EstadoEnum.values()));
		listaEstados.remove(0);
		return listaEstados;
	}

	public String getSigla() {
		return sigla;
	}

	public String getNome() {
		return nome;
	}

	public Long getId() {
		return id;
	}

}