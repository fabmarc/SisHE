package com.indra.sishe.controller.usuarioProjeto;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import com.indra.infra.resource.MessageProvider;
import com.indra.sishe.entity.Projeto;
import com.indra.sishe.entity.Usuario;
import com.indra.sishe.entity.UsuarioProjeto;

@ViewScoped
@ManagedBean(name = "usuarioProjetoCad")
public class UsuarioProjetoCadController extends UsuarioProjetoController {

	private static final long serialVersionUID = 5412878556555830463L;
	private List<Usuario> usuariosSelecionados = new ArrayList<Usuario>();

	@PostConstruct
	private void init() {
		MessageProvider.setInstance(messageProvider);
		searched = (Boolean) getFlashAttr("searched");
		usuarioProjetoSelecionado = (UsuarioProjeto) getSessionAttr("usuarioProjetoSelecionado");

		if (usuarioProjetoSelecionado == null) {
			usuarioProjetoSelecionado = new UsuarioProjeto();
		}

		usuarioProjetoFiltro = (UsuarioProjeto) getFlashAttr("usuarioProjetoFiltro");
		listarUsuarios(usuarioProjetoSelecionado);
	}

	public void UsuarioProjetoMntController() {

	}
	
	public String cancelar() {
		putFlashAttr("searched", searched);
		putFlashAttr("usuarioProjetoFiltro", usuarioProjetoFiltro);
		return irParaConsultar();
	}

	public List<Usuario> listarUsuarios(UsuarioProjeto usuarioProjeto) {
		listaUsuarios = usuarioProjetoService.findUserNotInProjeto(usuarioProjeto);
		return listaUsuarios;
	}

	public boolean modoCadastrar() {
		if (usuarioProjetoSelecionado == null || usuarioProjetoSelecionado.getId() == null) {
			cadastrarUsuarioProjeto();
			return true;
		} else {
			return false;
		}
	}

	public String cadastrarUsuarioProjeto() {
		usuarioProjetoService.salvar(usuariosSelecionados, (Projeto) getSessionAttr("projetoSelecionadoFiltro"));
		putFlashAttr("usuarioProjetoFiltro", usuarioProjetoFiltro);
		returnInfoMessage(messageProvider.getMessage("msg.success.registro.cadastrado", "Usuário(s)"));
		putFlashAttr("searched", searched);
		return irParaConsultar();
	}

	public List<Usuario> getListaUsuarios() {
		return listaUsuarios;
	}

	public void setListaUsuarios(List<Usuario> listaUsuarios) {
		this.listaUsuarios = listaUsuarios;
	}

	public List<Projeto> getListaProjetos() {
		return listaProjetos;
	}

	public void setListaProjetos(List<Projeto> listaProjetos) {
		this.listaProjetos = listaProjetos;
	}

	public UsuarioProjeto getUsuarioProjetoFiltro() {
		return usuarioProjetoFiltro;
	}

	public void setUsuarioProjetoFiltro(UsuarioProjeto usuarioProjetoFiltro) {
		this.usuarioProjetoFiltro = usuarioProjetoFiltro;
	}

	public UsuarioProjeto getUsuarioProjetoSelecionado() {
		return usuarioProjetoSelecionado;
	}

	public void setUsuarioProjetoSelecionado(UsuarioProjeto usuarioProjetoSelecionado) {
		this.usuarioProjetoSelecionado = usuarioProjetoSelecionado;
	}

	public List<UsuarioProjeto> getListaUsuarioProjeto() {
		return listaUsuarioProjeto;
	}

	public List<Usuario> getUsuariosProjetos() {
		return usuariosSelecionados;
	}

	public List<Usuario> getUsuariosSelecionados() {
		return usuariosSelecionados;
	}

	public void setUsuariosSelecionados(List<Usuario> usuariosSelecionados) {
		this.usuariosSelecionados = usuariosSelecionados;
	}

}
