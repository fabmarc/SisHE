package com.indra.sishe.controller.usuario;

import java.io.Serializable;

import javax.inject.Inject;

import com.indra.infra.controller.BaseController;
import com.indra.sishe.entity.Usuario;
import com.indra.sishe.service.UsuarioService;

public abstract class UsuarioController extends BaseController implements Serializable {

	private static final long serialVersionUID = -6070319180097929260L;

	@Inject
	protected transient UsuarioService usuarioService;

	protected Usuario usuarioFiltro;

	protected Boolean searched;

	public String irParaConsultar() {
		return "/paginas/usuario/consultarUsuario.xhtml";
	}

	public String irParaCadastrar() {
		putFlashAttr("usuarioSelecionado", null);
		putFlashAttr("searched", this.searched);
		putFlashAttr("usuarioFiltro", this.usuarioFiltro);
		return "/paginas/usuario/cadastrarUsuario.xhtml";
	}

	public String irParaAlterar() {
		return "/paginas/usuario/cadastrarUsuario.xhtml";
	}

	public String irParaAlterar(Usuario usuarioSelecionado) {
		putFlashAttr("searched", this.searched);
		putFlashAttr("usuarioFiltro", this.usuarioFiltro);
		putFlashAttr("usuarioSelecionado", usuarioSelecionado);
		return irParaAlterar();
	}

	public boolean validarUsuario(Usuario usuarioSelecionado) {
		if (usuarioSelecionado.getNome().isEmpty()) {
			messager.error(messageProvider.getMessage("msg.error.campo.obrigatorio", "Nome"));
		} else if (usuarioSelecionado.getNome().length() > 40) {
			messager.error(messageProvider.getMessage("msg.error.campo.maior.esperado", "Nome", "40"));
		} else {
			return true;
		}
		return false;
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

}
