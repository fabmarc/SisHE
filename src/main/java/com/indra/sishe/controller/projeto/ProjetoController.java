package com.indra.sishe.controller.projeto;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;

import com.indra.infra.controller.BaseController;
import com.indra.sishe.entity.Projeto;
import com.indra.sishe.service.ProjetoService;
import com.indra.sishe.service.UsuarioService;

public class ProjetoController extends BaseController implements Serializable {

	private static final long serialVersionUID = -3741340755153979444L;

	@Inject
	protected transient ProjetoService projetoService;

	@Inject
	protected transient UsuarioService usuarioService;

	public Projeto projetoFiltro;

	public List<Projeto> listaProjetos;
	
	protected Boolean searched;

	public boolean validarProjeto(Projeto projetoSelecionado) {
		if (projetoSelecionado.getNome().isEmpty()) {
			messager.error(messageProvider.getMessage("msg.error.campo.obrigatorio", "Nome"));
		} else if (projetoSelecionado.getNome().length() > 50) {
			messager.error(messageProvider.getMessage("msg.error.campo.maior.esperado", "Nome", "50"));
		} else if (projetoSelecionado.getGerente() == null) {
			messager.error(messageProvider.getMessage("msg.error.campo.obrigatorio", "Gerente"));
		} else if (projetoSelecionado.getDescricao().length() > 500) {
			messager.error(messageProvider.getMessage("msg.error.campo.maior.esperado", "Descrição", "500"));
		} else {
			return true;
		}
		return false;
	}

	public String irParaConsultar() {
		return "/paginas/projeto/consultarProjeto.xhtml?faces-redirect=true";
	}

	public String irParaAlterar() {
		return "/paginas/projeto/cadastrarProjeto.xhtml?faces-redirect=true";
	}

	public String irParaAlterar(Projeto projetoSelecionado) {
		putFlashAttr("searched", searched);
		putFlashAttr("projetoFiltro", projetoFiltro);
		putFlashAttr("projetoSelecionado", projetoSelecionado);
		return irParaAlterar();
	}	

	public String irParaCadastrar(){
		putFlashAttr("searched", searched);
		putFlashAttr("projetoFiltro", projetoFiltro);
		putFlashAttr("projetoSelecionado", null);
		return "/paginas/projeto/cadastrarProjeto.xhtml?faces-redirect=true";
	}

	public Projeto getProjetoFiltro() {
		return projetoFiltro;
	}

	public void setProjetoFiltro(Projeto projetoFiltro) {
		this.projetoFiltro = projetoFiltro;
	}

	public List<Projeto> getListaProjetos() {
		return listaProjetos;
	}

	public void setListaProjetos(List<Projeto> listaProjetos) {
		this.listaProjetos = listaProjetos;
	}
	
}
