package com.indra.sishe.enums;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum StatusEnum {

	Pendente(3L, "Pendente") ,Aprovada(1L, "Aprovada"), Reprovada(2L, "Reprovada");
	
	private final Long id;
	private final String nome;
	
	private StatusEnum(Long id, String nome) {
		this.id = id;
		this.nome = nome;
	}
	
	public static StatusEnum obterStatus(Long id){
		for (StatusEnum status : StatusEnum.values()){
			if (id == status.id) return status; 
		}
		return null;
	}
	
	public static List<StatusEnum> status(){
		List<StatusEnum> listaStatus = new ArrayList<StatusEnum>(Arrays.asList(StatusEnum.values()));
		return listaStatus;
	}

	public Long getId() {
		return id;
	}

	public String getNome() {
		return nome;
	}
	
}
