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
