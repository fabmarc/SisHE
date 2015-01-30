package com.indra.infra.controller;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.inject.Inject;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;

import com.indra.infra.util.FacesMessager;
import com.indra.sishe.entity.Usuario;
import com.indra.sishe.service.UsuarioService;

@ManagedBean
@RequestScoped
public class LoginController extends BaseController{

	private String username = null;
	private String password = null;

	@ManagedProperty(value = "#{authenticationManager}")
	private AuthenticationManager authenticationManager = null;
	
	@Inject
	private FacesMessager messager;
	
	@Inject
	private transient UsuarioService usuarioService;

	public LoginController() {
	}

	public String login() {
		try {
			Authentication request = new UsernamePasswordAuthenticationToken(getUsername(), getPassword());
			Authentication result = authenticationManager.authenticate(request);
			SecurityContextHolder.getContext().setAuthentication(result);		
			Usuario user = new Usuario();
			user.setLogin(this.username);
			user = usuarioService.findByLogin(username);
			putSessionAttr("usuario_login", this.username);	
			putSessionAttr("usuario_id", user.getId());
			putSessionAttr("usuario_permissoes", result.getAuthorities().toString());
		} catch (AuthenticationException e) {
			messager.error(e.getMessage());
			e.printStackTrace();
			return null;
		}
		return "/index.xhtml?faces-redirect=true";
	}

	public String cancel() {
		return null;
	}

	public String logout() {
		SecurityContextHolder.clearContext();
		return "/unsecure/login.jsf";
	}

	public AuthenticationManager getAuthenticationManager() {
		return authenticationManager;
	}

	public void setAuthenticationManager(AuthenticationManager authenticationManager) {
		this.authenticationManager = authenticationManager;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
