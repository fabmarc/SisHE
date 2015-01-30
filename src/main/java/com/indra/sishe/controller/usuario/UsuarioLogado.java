package com.indra.sishe.controller.usuario;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;

import com.indra.infra.controller.BaseController;
import com.indra.infra.resource.MessageProvider;
import com.indra.infra.service.exception.ApplicationException;
import com.indra.sishe.entity.Usuario;
import com.indra.sishe.service.UsuarioService;


@ViewScoped
@ManagedBean(name = "usuarioLogado")
public class UsuarioLogado extends BaseController implements Serializable {


	private static final long serialVersionUID = 3890881279155488397L;
	
	private Usuario usuario;
	
	@Inject
	private UsuarioService usuarioService;
	
	@PostConstruct
	private void init() {
		MessageProvider.setInstance(messageProvider);
	}
	
	public UsuarioLogado(){
		usuario = new Usuario();
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
	
	public String alterarSenha() {
		try {
			usuario.setId(getId());
			usuarioService.alterarSenha(usuario);
			returnInfoMessage(messageProvider.getMessage("msg.error.senha.alterada"));
			return "/index.xhtml?faces-redirect=true";
		} catch (ApplicationException e) {
			returnErrorMessage(e.getMessage());
			return null;
		}
	}
	
	
	public Usuario getUsuario() {
		return usuario;
	}
	
	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
	
}
