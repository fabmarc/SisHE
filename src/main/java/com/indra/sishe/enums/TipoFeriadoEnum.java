package com.indra.sishe.enums;

public enum TipoFeriadoEnum {
	Fixo('F', "Fixo"), Movel('M', "Móvel");
	private final Character idTipo;
	private final String nomeTipo;

	private TipoFeriadoEnum(Character idTipo, String nomeTipo) {
		this.idTipo = idTipo;
		this.nomeTipo = nomeTipo;
	} 
	
	public String nomeTipoFeriado(){
		return this.nomeTipo;
	}
	
	public Character tipoFeriado(){
		return this.idTipo;
	}
	
	public static String obterNomeTipo(char letraTipo){
		for (TipoFeriadoEnum tipo : TipoFeriadoEnum.values()){
			if (letraTipo == tipo.idTipo) return tipo.nomeTipoFeriado();
		}
		return null;
	}
	
	public static TipoFeriadoEnum obterTipo(char letraTipo){
		for (TipoFeriadoEnum tipo : TipoFeriadoEnum.values()){
			if (letraTipo == tipo.idTipo) return tipo;
		}
		return null;
	}
	
}
