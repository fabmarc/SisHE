package com.indra.sishe.controller.usuario;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.servlet.http.HttpSession;

import com.indra.infra.controller.BaseController;
import com.indra.infra.resource.MessageProvider;
import com.indra.infra.service.exception.ApplicationException;
import com.indra.sishe.entity.Usuario;
import com.indra.sishe.service.BancoHorasService;
import com.indra.sishe.service.UsuarioService;

@ViewScoped
@ManagedBean(name = "usuarioLogado")
public class UsuarioLogado extends BaseController implements Serializable {

	private static final long serialVersionUID = 3890881279155488397L;

	private Usuario usuario;

	@Inject
	private UsuarioService usuarioService;
	
	@Inject
	private BancoHorasService bancoService;

	@PostConstruct
	private void init() {
		MessageProvider.setInstance(messageProvider);
	}

	public UsuarioLogado() {
		usuario = new Usuario();
	}

	public String getLogin() {
		return (String) getSessionAttr("usuario_login");
	}

	public static Long getId() {
		return (Long) ((HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true))
				.getAttribute("usuario_id");
	}

	public String getNome() {
		return (String) getSessionAttr("usuario_nome");
	}

	public Long getSaldoHoras() {
		return (Long) getSessionAttr("saldo");
	}

	public String getSaldoFormatado() {
		
		Long saldo = (Long) getSessionAttr("saldo");
		Long min, horas;
		horas = saldo / 60;
		min = saldo % 60;
		return horas + " h e " + min + " min";
	}

	public static String getPermissoes() {
		return (String) ((HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true))
				.getAttribute("usuario_permissoes");
	}

	public static boolean verificarPermissao(String nivel) {
		if (getPermissoes().contains(nivel)) {
			return true;
		} else {
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
