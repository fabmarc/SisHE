package com.indra.sishe.controller.usuario;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import com.indra.infra.controller.BaseController;


@ViewScoped
@ManagedBean(name = "usuarioLogado")
public class UsuarioLogado extends BaseController implements Serializable {

	private static final long serialVersionUID = 3890881279155488397L;
	

	public UsuarioLogado(){
	}

	public String getLogin() {
		return (String) getSessionAttr("usuario_login");
	}
	
	public Long getId(){
		return (Long) getSessionAttr("usuario_id");
	}
	
	public String getNome(){
		return (String) getSessionAttr("usuario_nome");
	}
	
	public String getPermissoes(){
		return (String) getSessionAttr("usuario_permissoes");
	}
		
	public boolean verificarPermissao(String nivel){
		if(getPermissoes().contains(nivel)){			
			return true;
		}else{
			return false;
		}
	}	
	
}
