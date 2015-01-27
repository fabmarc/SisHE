package com.indra.sishe.enums;

public enum EstadoEnum {

	BR("BR", "Nacional", 0L), AC("AC", "Acre", 1L), AL("AL", "Alagoas", 2L), AP("AP", "Amapá", 3L), AM("AM", "Amazonas", 4L),
	BA("BA", "Bahia", 5L), CE("CE", "Ceará", 6L), DF("DF", "Distrito Federal", 7L), ES("ES", "Espírito Santo", 8L), 
	GO("GO", "Goiás", 9L), MA("MA", "Maranhão", 10L), MT("MT", "Mato Grosso", 11L), MS("MS", "Mato Grosso do Sul", 12L), 
	MG("MG", "Minas Gerais", 13L), PA("PA", "Pará", 14L), PB("PB", "Paraíba", 15L), PR("PR", "Paraná", 16L), 
	PE("PE", "Pernambuco", 17L), PI("PI", "Piauí", 18L), RJ("RJ", "Rio de Janeiro", 19L), RN("RN", "Rio Grande do Norte", 20L),
	RS("RS", "Rio Grande do Sul", 21L), RO("RO", "Rondônia", 22L), RR("RR", "Roraima", 23L), SC("SC", "Santa Catarina", 24L),
	SP("SP", "São Paulo", 25L), SE("SE", "Sergipe", 26L), TO("TO", "Tocantins", 27L);

	private final String sigla;   
	private final String nome;
	private final Long id;  

	private EstadoEnum(String sigla, String nome, Long id) {		
		this.sigla = sigla;
		this.nome = nome;
		this.id = id;
	}

public static EstadoEnum obterEstado(Long id){
	for (EstadoEnum estado : EstadoEnum.values()){
		if (id == estado.id) return estado;
	}
	return null;
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