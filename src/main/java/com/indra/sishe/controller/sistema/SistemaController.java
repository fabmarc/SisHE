package com.indra.sishe.controller.sistema;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;

import com.indra.infra.controller.BaseController;
import com.indra.sishe.entity.Sistema;
import com.indra.sishe.service.SistemaService;

public class SistemaController extends BaseController implements Serializable {

	private static final long serialVersionUID = -2097043422349464567L;
	
	@Inject
	protected  SistemaService sistemaService;
	
	protected List<Sistema> listaSistema;

	// VARIÁVEL UTILIZADA PARA O FILTRO DA PESQUISA
	public Sistema sistemaFiltro;

	// VARIÁVEL UTILIZADA PARA EXCLUIR OU ALTERAR
	public Sistema sistemaSelecionado;

	// TRUE QUANDO O BOTÃO PESQUISAR FOR PRESSIONADO
	protected Boolean searched;

	public SistemaController() {

	}

	public boolean validarSistema(Sistema sistemaSelecionado) {
		
		if (sistemaSelecionado.getNome() == null || sistemaSelecionado.getNome().isEmpty()) {
			messager.error(messageProvider.getMessage("msg.error.campo.obrigatorio", "Nome"));
		} else if (sistemaSelecionado.getNome() == null || sistemaSelecionado.getNome().length() > 50) {
			messager.error(messageProvider.getMessage("msg.error.campo.maior.esperado", "Nome", "50"));
		} else if (sistemaSelecionado.getProjeto().getId() == null) {
			messager.error(messageProvider.getMessage("msg.error.campo.obrigatorio", "Projeto"));
		} else if (sistemaSelecionado.getLider().getId() == null) {
			messager.error(messageProvider.getMessage("msg.error.campo.obrigatorio", "Lider"));
		} else {
			return true;
		}
		return false;
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
