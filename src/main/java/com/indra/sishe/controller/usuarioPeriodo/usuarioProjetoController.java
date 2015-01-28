package com.indra.sishe.controller.usuarioPeriodo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import com.indra.infra.controller.BaseController;
import com.indra.sishe.entity.Projeto;
import com.indra.sishe.entity.Usuario;
import com.indra.sishe.entity.UsuarioProjeto;
import com.indra.sishe.service.UsuarioProjetoService;

public class usuarioProjetoController extends BaseController implements Serializable {

	private static final long serialVersionUID = -6245245686300031889L;

	@Inject
	protected UsuarioProjetoService usuarioProjetoService;

	protected List<UsuarioProjeto> usuarioProjeto;

	protected List<Usuario> listaLideres = new ArrayList<Usuario>();
	protected List<Projeto> listaProjetos = new ArrayList<Projeto>();

	// VARIÁVEL UTILIZADA PARA O FILTRO DA PESQUISA
	public UsuarioProjeto usuarioProjetoFiltro;

	// VARIÁVEL UTILIZADA PARA EXCLUIR OU ALTERAR
	public UsuarioProjeto usuarioProjetoSelecionado;

	// TRUE QUANDO O BOTÃO PESQUISAR FOR PRESSIONADO
	protected Boolean searched;

	public usuarioProjetoController() {
	}

	public String irParaConsultar() {
		putFlashAttr("usuarioProjetoSelecionado", null);
		return "/paginas/equipe/consultarEquipe.xhtml?faces-redirect=true";
	}

	public String irParaCadastrar() {
		putFlashAttr("searched", searched);
		putFlashAttr("usuarioProjetoFiltro", usuarioProjetoFiltro);
		putFlashAttr("usuarioProjetoSelecionado", null);
		return "/paginas/equipe/cadastrarEquipe.xhtml?faces-redirect=true";
	}

	public String irParaAlterar(UsuarioProjeto usuarioProjeto) {
		putFlashAttr("searched", searched);
		putFlashAttr("usuarioProjetoFiltro", usuarioProjetoFiltro);
		putFlashAttr("usuarioProjetoSelecionado", usuarioProjetoSelecionado);
		return "/paginas/equipe/cadastrarEquipe.xhtml";
	}

}
