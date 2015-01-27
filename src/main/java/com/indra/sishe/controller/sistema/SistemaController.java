package com.indra.sishe.controller.sistema;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import com.indra.infra.controller.BaseController;
import com.indra.sishe.entity.Projeto;
import com.indra.sishe.entity.Sistema;
import com.indra.sishe.entity.Usuario;
import com.indra.sishe.service.SistemaService;

public class SistemaController extends BaseController implements Serializable {

	private static final long serialVersionUID = -2097043422349464567L;

	@Inject
	protected SistemaService sistemaService;

	protected List<Sistema> listaSistema;

	protected List<Usuario> listaLideres = new ArrayList<Usuario>();
	protected List<Projeto> listaProjetos = new ArrayList<Projeto>();

	// VARIÁVEL UTILIZADA PARA O FILTRO DA PESQUISA
	public Sistema sistemaFiltro;

	// VARIÁVEL UTILIZADA PARA EXCLUIR OU ALTERAR
	public Sistema sistemaSelecionado;

	// TRUE QUANDO O BOTÃO PESQUISAR FOR PRESSIONADO
	protected Boolean searched;

	public SistemaController() {

	}

	public String irParaConsultar() {
		putFlashAttr("sistemaSelecionado", null);
		return "/paginas/sistema/consultarSistema.xhtml?faces-redirect=true";
	}

	public String irParaCadastrar() {
		putFlashAttr("searched", searched);
		putFlashAttr("sistemaFiltro", sistemaFiltro);
		putFlashAttr("sistemaSelecionado", null);
		return "/paginas/sistema/cadastrarSistema.xhtml?faces-redirect=true";
	}

	public String irParaAlterar(Sistema sistemaSelecionado) {
		putFlashAttr("searched", searched);
		putFlashAttr("sistemaFiltro", sistemaFiltro);
		putFlashAttr("sistemaSelecionado", sistemaSelecionado);
		return "/paginas/sistema/cadastrarrSistema.xhtml";
	}

}
