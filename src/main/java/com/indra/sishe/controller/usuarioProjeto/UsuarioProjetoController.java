package com.indra.sishe.controller.usuarioProjeto;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import com.indra.infra.controller.BaseController;
import com.indra.sishe.entity.Projeto;
import com.indra.sishe.entity.Usuario;
import com.indra.sishe.entity.UsuarioProjeto;
import com.indra.sishe.service.CargoService;
import com.indra.sishe.service.ProjetoService;
import com.indra.sishe.service.UsuarioProjetoService;
import com.indra.sishe.service.UsuarioService;

public class UsuarioProjetoController extends BaseController implements Serializable {

	private static final long serialVersionUID = -6245245686300031889L;

	@Inject
	protected UsuarioProjetoService usuarioProjetoService;

	protected List<UsuarioProjeto> usuarioProjeto;
	protected List<UsuarioProjeto> listaUsuarioProjeto = new ArrayList<UsuarioProjeto>();
	
	@Inject
	protected ProjetoService projetoService;

	@Inject
	protected UsuarioService usuarioService;

	@Inject
	protected CargoService cargoService;

	protected List<Usuario> listaUsuarios = new ArrayList<Usuario>();
	protected List<Projeto> listaProjetos = new ArrayList<Projeto>();

	// VARIÁVEL UTILIZADA PARA O FILTRO DA PESQUISA
	public UsuarioProjeto usuarioProjetoFiltro ;

	// VARIÁVEL UTILIZADA PARA EXCLUIR OU ALTERAR
	public UsuarioProjeto usuarioProjetoSelecionado;

	public Projeto projetoSelecionado;
	
	// TRUE QUANDO O BOTÃO PESQUISAR FOR PRESSIONADO
	protected Boolean searched;

	public UsuarioProjetoController() {

	}

	public String irParaConsultar() {
		putFlashAttr("usuarioProjetoSelecionado", null);
		return "/paginas/equipe/consultarEquipe.xhtml?faces-redirect=true";
	}

	public String irParaCadastrar() {
		putFlashAttr("searched", searched);
		putFlashAttr("usuarioProjetoFiltro", usuarioProjetoFiltro);
		putSessionAttr("usuarioProjetoSelecionado", usuarioProjetoSelecionado);
		return "/paginas/equipe/cadastrarEquipe.xhtml?faces-redirect=true";
	}

	public List<UsuarioProjeto> getUsuarioProjeto() {
		return usuarioProjeto;
	}

	public void setUsuarioProjeto(List<UsuarioProjeto> usuarioProjeto) {
		this.usuarioProjeto = usuarioProjeto;
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

	public Projeto getProjetoSelecionado() {
		return projetoSelecionado;
	}

	public void setProjetoSelecionado(Projeto projetoSelecionado) {
		this.projetoSelecionado = projetoSelecionado;
	}

}

