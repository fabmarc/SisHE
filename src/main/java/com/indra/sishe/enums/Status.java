package com.indra.sishe.enums;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum Status {

//	AgLider(1L, "Aguardando Líder"), RpLider(2L, "Reprovado pelo Líder"), AgGerente(3L, "Aguardando Gerente"), RpGerente(4L, "Reprovado pelo Gerente"), Concluida(5L, "Concluída");
	Aprovada(1L, "Aprovada"), Reprovada(2L, "Reprovada"), Pendente(3L, "Pendente");
	
	private final Long id;
	private final String nome;
	
	private Status(Long id, String nome) {
		this.id = id;
		this.nome = nome;
	}
	
	public static Status obterStatus(Long id){
		for (Status status : Status.values()){
			if (id == status.id) return status; 
		}
		return null;
	}
	
	public static List<Status> status(){
		List<Status> listaStatus = new ArrayList<Status>(Arrays.asList(Status.values()));
		return listaStatus;
	}

	public Long getId() {
		return id;
	}

	public String getNome() {
		return nome;
	}
	
}
