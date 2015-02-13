package com.indra.sishe.enums;

public enum PermissaoEnum {
	
	ADMINISTRADOR("Administrador", "ROLE_ADMIN"),
	GERENTE("Gerente", "ROLE_GERENTE, ROLE_FUNCIONARIO"),
	LIDER("Líder", "ROLE_LIDER, ROLE_FUNCIONARIO"),
	FUNCIONARIO("Funcionário", "ROLE_FUNCIONARIO");
	
	private String permissao;
	private String role;
	
	PermissaoEnum(String permissao, String role){
		this.permissao = permissao;
		this.role = role;
	}
	
	public String getPermissao(){
		return this.permissao;
	}
	
	public String getRole(){
		return this.role;
	}
		
	public static PermissaoEnum obterPermissao(String role) {
		for (PermissaoEnum per : PermissaoEnum.values()) {
			if (role.equalsIgnoreCase(per.getRole())) return per;
		}
		return null;
	}

}
