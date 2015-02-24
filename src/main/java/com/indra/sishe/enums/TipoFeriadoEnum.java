package com.indra.sishe.enums;

public enum TipoFeriadoEnum {
	Fixo('F'), Movel('M');
	private final Character idTipo;

	private TipoFeriadoEnum(Character idTipo) {
		this.idTipo = idTipo;
	} 
	
	public Character tipoFeriado(){
		return this.idTipo;
	}
	
	public static TipoFeriadoEnum obterTipo(char letraTipo){
		for (TipoFeriadoEnum tipo : TipoFeriadoEnum.values()){
			if (letraTipo == tipo.idTipo) return tipo;
		}
		return null;
	}
	
}
