package com.indra.sishe.controller.usuario;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;

import com.indra.infra.controller.BaseController;
import com.indra.sishe.entity.Cargo;
import com.indra.sishe.entity.Usuario;
import com.indra.sishe.service.CargoService;
import com.indra.sishe.service.UsuarioService;

public abstract class UsuarioController extends BaseController implements Serializable {

	private static final long serialVersionUID = -6070319180097929260L;

	@Inject
	protected transient UsuarioService usuarioService;

	@Inject
	protected transient CargoService cargoService;

	protected Usuario usuarioFiltro;

	protected Boolean searched;

	public String irParaConsultar() {
		
		return "/paginas/usuario/consultarUsuario.xhtml?faces-redirect=true";
	}

	public String irParaCadastrar() {
		
		putFlashAttr("usuarioSelecionado", null);
		putFlashAttr("searched", this.searched);
		putFlashAttr("usuarioFiltro", this.usuarioFiltro);
		return "/paginas/usuario/cadastrarUsuario.xhtml?faces-redirect=true";
	}

	public String irParaAlterar() {
		
		return "/paginas/usuario/cadastrarUsuario.xhtml?faces-redirect=true";
	}

	public String irParaAlterar(Usuario usuarioSelecionado) {
		
		putFlashAttr("searched", this.searched);
		putFlashAttr("usuarioFiltro", this.usuarioFiltro);
		putFlashAttr("usuarioSelecionado", usuarioSelecionado);
		return irParaAlterar();
	}

	public Usuario getUsuarioFiltro() {
		
		return usuarioFiltro;
	}

	public void setUsuarioFiltro(Usuario usuarioFiltro) {
		
		this.usuarioFiltro = usuarioFiltro;
	}

	public Boolean wasSearched() {
		
		return searched;
	}

	public void setSearched(Boolean searched) {
		
		this.searched = searched;
	}

	public List<Cargo> obterCargos() {
		
		return cargoService.findAll();
	}
	
}
